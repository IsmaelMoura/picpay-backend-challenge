package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

private val logger = KotlinLogging.logger {}

class MockTransferAuthorizationService(
    private val webClient: WebClient,
    private val metricsModule: AuthorizationMetricsModule,
) : TransferAuthorizationService {
    override suspend fun isAuthorized(): Boolean {
        return runCatching {
            metricsModule.measureGetAuthorizationData {
                webClient
                    .also { logger.info { "Sending request to check transfer authorization" } }
                    .get()
                    .retrieve()
                    .awaitBody<AuthorizationResponse>()
                    .isAuthorized
            }
        }.onSuccess { isAuthorized ->
            logger.info { "Retrieved response from authorization service (isAuthorized: $isAuthorized)" }
        }.onFailure {
            logger.warn(it) { "Occurred error while to check transfer authorization" }
        }.getOrElse { false }
    }
}
