package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.model.Transfer

data class SendNotificationRequest(
    val transfer: Transfer,
    val channels: Set<Channel>,
) {
    enum class Channel {
        EMAIL,
        SMS,
    }
}
