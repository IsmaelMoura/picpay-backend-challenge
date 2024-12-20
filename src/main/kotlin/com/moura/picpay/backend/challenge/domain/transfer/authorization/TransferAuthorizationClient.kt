package com.moura.picpay.backend.challenge.domain.transfer.authorization

fun interface TransferAuthorizationClient {
    suspend fun isAuthorized(): Boolean
}
