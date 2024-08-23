package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import io.micrometer.observation.ObservationRegistry
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions

@Configuration
class NotificationSenderConfiguration {
    @Bean
    fun reactiveKafkaProducerTemplate(
        properties: KafkaProperties,
        observation: ObservationRegistry,
    ): ReactiveKafkaProducerTemplate<TransferId, ByteArray> {
        val props = properties.buildProducerProperties(null)
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = TransferIdKafkaKeySerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = ByteArraySerializer::class.java

        return ReactiveKafkaProducerTemplate(
            SenderOptions.create<TransferId, ByteArray>(props)
                .withObservation(observation),
        )
    }
}
