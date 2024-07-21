package com.moura.picpay.backend.challenge.domain.transfer.authorization

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("picpay-backend-challenge.transfer.authorization")
data class AuthorizationServiceProperties(val url: String)