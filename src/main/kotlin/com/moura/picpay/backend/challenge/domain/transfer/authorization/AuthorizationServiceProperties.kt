package com.moura.picpay.backend.challenge.domain.transfer.authorization

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("picpay-backend-challenge.transfer.authorization")
data class AuthorizationServiceProperties(
    private val host: String,
    private val path: String,
) {
    val baseUrl = "$host$path"
}
