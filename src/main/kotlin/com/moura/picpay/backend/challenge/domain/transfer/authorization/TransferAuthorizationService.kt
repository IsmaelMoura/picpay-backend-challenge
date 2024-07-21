package com.moura.picpay.backend.challenge.domain.transfer.authorization

sealed interface TransferAuthorizationService {

    suspend fun isAuthorized(): Boolean
}