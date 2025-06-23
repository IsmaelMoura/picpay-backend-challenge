package com.moura.picpay.backend.challenge.infrastructure.kafka.producer

import com.moura.picpay.backend.challenge.domain.transfer.model.Transfer
import com.moura.picpay.backend.challenge.domain.transfer.notification.NotificationMetricsModule
import com.moura.picpay.backend.challenge.domain.transfer.notification.TransferNotificationSender
import com.moura.picpay.backend.challenge.notification.NotificationChannel
import com.moura.picpay.backend.challenge.notification.SendTransferNotificationRequest
import com.moura.picpay.backend.challenge.notification.sendTransferNotificationRequest
import com.moura.picpay.backend.challenge.notification.transfer
import com.moura.picpay.backend.challenge.notification.transferAmount
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.future.await
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class KafkaTransferNotificationSender(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val properties: KafkaNotificationProperties,
    private val metrics: NotificationMetricsModule,
) : TransferNotificationSender {
    override suspend fun send(transfer: Transfer): Result<Unit> =
        metrics.measureSendNotificationRequest {
            runCatching {
                kafkaTemplate.send(createProducerRecord(transfer)).await()
            }.onSuccess { result ->
                with(result.recordMetadata) {
                    logger.info {
                        "Transfer notification successfully sent to kafka (" +
                            "transferId: ${transfer.id}, " +
                            "partition: ${partition()}, " +
                            "offset: ${offset()}, " +
                            "topic: ${topic()})."
                    }
                }
            }.onFailure { throwable ->
                logger.warn(throwable) { "Error while sending notification for transfer [${transfer.id}]" }
            }.map { }
        }

    private fun createProducerRecord(transfer: Transfer): ProducerRecord<String, ByteArray> =
        ProducerRecord(
            properties.topic,
            transfer.id.value,
            createSendTransferNotificationRequest(transfer).toByteArray(),
        )

    private fun createSendTransferNotificationRequest(transfer: Transfer): SendTransferNotificationRequest =
        sendTransferNotificationRequest {
            this.transfer =
                transfer {
                    transferId = transfer.id.value
                    payeeUserId = transfer.payee.id.value
                    payerUserId = transfer.payer.id.value
                    amount =
                        transferAmount {
                            scale = transfer.amount.scale()
                            value = transfer.amount.toDouble()
                        }
                }
            channels.addAll(
                setOf(
                    NotificationChannel.NOTIFICATION_CHANNEL_SMS,
                    NotificationChannel.NOTIFICATION_CHANNEL_EMAIL,
                ),
            )
        }
}
