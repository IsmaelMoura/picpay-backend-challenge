package com.moura.picpay.backend.challenge.domain.transfer.notification

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("picpay-backend-challenge.transfer.notification")
data class NotificationSenderProperties(val url: String) {
}