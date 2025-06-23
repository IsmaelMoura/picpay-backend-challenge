package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import java.io.Serializable

data class AuthorizationResponse(
    val data: Data,
) : Serializable {
    val isAuthorized: Boolean = data.authorization

    data class Data(
        val authorization: Boolean,
    ) : Serializable
}
