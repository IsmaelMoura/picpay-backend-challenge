package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import org.apache.kafka.common.serialization.Serializer

class TransferIdKafkaKeySerializer : Serializer<TransferId> {
    override fun serialize(
        topic: String,
        data: TransferId,
    ): ByteArray {
        return data.value.toByteArray(Charsets.UTF_8)
    }
}
