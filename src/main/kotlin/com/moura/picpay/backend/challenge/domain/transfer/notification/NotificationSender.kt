package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.Transfer

fun interface NotificationSender {
    suspend fun sendNotification(transfer: Transfer): SendResult
}
