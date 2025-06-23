package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.model.Transfer

fun interface TransferNotificationSender {
    suspend fun send(transfer: Transfer): Result<Unit>
}
