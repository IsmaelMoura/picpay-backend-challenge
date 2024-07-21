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
    private object Container : ComposeContainer(File("docker-compose.yaml")) {
        private const val POSTGRES_SERVICE_NAME = "postgres"
        private const val POSTGRES_PORT = 5432
        private const val MOCKOON_SERVICE_NAME = "mockoon"
        private const val MOCKOON_PORT = 9090

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

            val transferAuthorizationUrl = "http://$serviceHost:$servicePort/api/v1/transfers/authorization"
            val transferNotificationUrl = "http://$serviceHost:$servicePort/api/v1/transfers/notification"
        }

        init {
            withExposedService(POSTGRES_SERVICE_NAME, POSTGRES_PORT)
                .withExposedService(MOCKOON_SERVICE_NAME, MOCKOON_PORT)
                .start()
        }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", Container.Postgres::r2dbcUrl)
            registry.add("spring.r2dbc.username", Container.Postgres::USERNAME)
            registry.add("spring.r2dbc.password", Container.Postgres::PASSWORD)
            registry.add("spring.flyway.url", Container.Postgres::flywayUrl)
            registry.add("spring.flyway.username", Container.Postgres::USERNAME)
            registry.add("spring.flyway.password", Container.Postgres::PASSWORD)

            registry.add("picpay-backend-challenge.transfer.authorization.url", Container.Mockoon::transferAuthorizationUrl)
            registry.add("picpay-backend-challenge.transfer.notification.url", Container.Mockoon::transferNotificationUrl)
        }
    }
}
