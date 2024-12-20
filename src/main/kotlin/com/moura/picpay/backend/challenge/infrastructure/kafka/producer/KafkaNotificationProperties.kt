package com.moura.picpay.backend.challenge.infrastructure.kafka.producer

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.transfer.notification.kafka")
data class KafkaNotificationProperties(
    val topic: String,
)
