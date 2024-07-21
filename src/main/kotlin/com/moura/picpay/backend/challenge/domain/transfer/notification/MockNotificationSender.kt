package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.Transfer
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.web.reactive.function.client.WebClient

private val logger = KotlinLogging.logger {}

class MockNotificationSender(private val webClient: WebClient) : NotificationSender {

    override suspend fun sendNotification(transfer: Transfer): SendResult {
        return sendCatching {
            webClient.post()
                .bodyValue(
                    SendNotificationRequest(
                        transfer = transfer,
                        channels = setOf(SendNotificationRequest.Channel.SMS, SendNotificationRequest.Channel.EMAIL)
                    )
                )
                .also {
                    logger.info { "Sending notification request for transfer [${transfer.id}]" }
                }
                .retrieve()
                .toBodilessEntity()
                .awaitSingle()
        }.onFailure {
            logger.warn(it) { "Unexpected error while sending notification of transfer [${transfer.id}]" }
        }.onSuccess {
            logger.info { "Successfully sent notification of transfer [${transfer.id}]" }
        }
    }
}