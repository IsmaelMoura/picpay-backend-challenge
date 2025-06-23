@file:Suppress("HttpUrlsUsage")

package com.moura.picpay.backend.challenge

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.ComposeContainer
import java.io.File

@SpringBootTest
@Tag("integrationTest")
@ActiveProfiles("test")
class IntegrationTest {
    companion object : ComposeContainer(File("docker-compose.yaml")) {
        private const val POSTGRES_SERVICE_NAME = "postgres"
        private const val POSTGRES_PORT = 5432
        private const val MOCKOON_SERVICE_NAME = "mockoon"
        private const val MOCKOON_PORT = 9090
        private const val KAFKA_SERVICE_NAME = "kafka"
        private const val KAFKA_PORT = 9092

        object Postgres {
            const val USERNAME = "root"
            const val PASSWORD = "root"

            private val serviceHost: String = getServiceHost(POSTGRES_SERVICE_NAME, POSTGRES_PORT)
            private val servicePort: Int = getServicePort(POSTGRES_SERVICE_NAME, POSTGRES_PORT)
            val r2dbcUrl: String = "r2dbc:postgresql://$serviceHost:$servicePort/picpay_simplified"
            val flywayUrl: String = "jdbc:postgresql://$serviceHost:$servicePort/picpay_simplified"
        }

        object Mockoon {
            private val serviceHost = getServiceHost(MOCKOON_SERVICE_NAME, MOCKOON_PORT)
            private val servicePort = getServicePort(MOCKOON_SERVICE_NAME, MOCKOON_PORT)

            val host = "http://$serviceHost:$servicePort"
        }

        object Kafka {
            private val host = getServiceHost(KAFKA_SERVICE_NAME, KAFKA_PORT)
            private val port = getServicePort(KAFKA_SERVICE_NAME, KAFKA_PORT)
            val bootstrapServers = "$host:$port"
        }

        init {
            withExposedService(POSTGRES_SERVICE_NAME, POSTGRES_PORT)
                .withExposedService(MOCKOON_SERVICE_NAME, MOCKOON_PORT)
                .withExposedService(KAFKA_SERVICE_NAME, KAFKA_PORT)
                .start()
        }

        @Suppress("unused")
        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.apply {
                add("spring.r2dbc.url", Postgres::r2dbcUrl)
                add("spring.r2dbc.username", Postgres::USERNAME)
                add("spring.r2dbc.password", Postgres::PASSWORD)
                add("spring.flyway.url", Postgres::flywayUrl)
                add("spring.flyway.user", Postgres::USERNAME)
                add("spring.flyway.password", Postgres::PASSWORD)

                add("application.transfer.authorization.host", Mockoon::host)

                add("spring.kafka.bootstrap-servers", Kafka::bootstrapServers)
            }
        }
    }
}
