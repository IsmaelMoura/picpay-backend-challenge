package com.moura.picpay.backend.challenge.infrastructure.kafka.producer

import io.micrometer.observation.ObservationRegistry
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaNotificationSenderConfiguration {
    @Bean
    fun reactiveKafkaProducerTemplate(
        properties: KafkaProperties,
        observation: ObservationRegistry,
    ): ReactiveKafkaProducerTemplate<String, ByteArray> {
        val props = properties.buildProducerProperties(null)

        return ReactiveKafkaProducerTemplate(
            SenderOptions.create<String, ByteArray>(props)
                .withObservation(observation)
                .withKeySerializer(StringSerializer())
                .withValueSerializer(ByteArraySerializer()),
        )
    }
}
