package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.transfer.authorization")
data class AuthorizationClientProperties(
    private val host: String,
    private val path: String,
) {
    val baseUrl = "$host/$path"
}
