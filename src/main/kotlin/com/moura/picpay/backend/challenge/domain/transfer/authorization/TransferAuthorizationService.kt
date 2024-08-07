package com.moura.picpay.backend.challenge.domain.transfer.authorization

fun interface TransferAuthorizationService {
    suspend fun isAuthorized(): Boolean
}
