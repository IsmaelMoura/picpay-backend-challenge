package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.model.Transfer
import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.notification.NotificationChannel
import com.moura.picpay.backend.challenge.notification.SendTransferNotificationRequest
import com.moura.picpay.backend.challenge.notification.sendTransferNotificationRequest
import com.moura.picpay.backend.challenge.notification.transfer
import com.moura.picpay.backend.challenge.notification.transferAmount
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.reactor.awaitSingle
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.kafka.sender.SenderResult

private val logger = KotlinLogging.logger {}

@Component
class KafkaNotificationSender(
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<TransferId, ByteArray>,
    private val properties: KafkaNotificationProperties,
    private val metrics: NotificationMetricsModule,
) : NotificationSender {
    override suspend fun sendNotification(transfer: Transfer): SendResult {
        return metrics.measureSendNotificationRequest {
            sendCatching {
                kafkaTemplate
                    .send(createProducerRecord(transfer))
                    .awaitSingle()
                    .throwOnFailure()
            }
                .onSuccess {
                    logger.info { "Transfer [${transfer.id}] notification successfully sent to [${properties.topic}] topic" }
                }
                .onFailure { throwable ->
                    logger.warn(throwable) { "Error while sending notification for transfer [${transfer.id}]" }
                }
        }
    }

    private fun createProducerRecord(transfer: Transfer): ProducerRecord<TransferId, ByteArray> {
        return ProducerRecord(
            properties.topic,
            transfer.id,
            createSendTransferNotificationRequest(transfer).toByteArray(),
        )
    }

    private fun createSendTransferNotificationRequest(transfer: Transfer): SendTransferNotificationRequest {
        return sendTransferNotificationRequest {
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

    private fun SenderResult<Void>.throwOnFailure() {
        if (exception() != null) {
            throw exception()
        }
    }
}
