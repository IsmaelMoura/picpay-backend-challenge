package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import com.moura.picpay.backend.challenge.domain.transfer.authorization.AuthorizationMetricsModule
import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class HttpTransferAuthorizationClient(
    builder: WebClient.Builder,
    properties: AuthorizationClientProperties,
    private val metrics: AuthorizationMetricsModule,
) : TransferAuthorizationClient {
    private val logger = KotlinLogging.logger {}
    private val webClient = builder.clone().baseUrl(properties.baseUrl).build()

    override suspend fun isAuthorized(): Boolean {
        return runCatching {
            metrics.measureGetAuthorizationData {
                webClient
                    .also { logger.info { "Sending request to check transfer authorization" } }
                    .get()
                    .retrieve()
                    .awaitBody<AuthorizationResponse>()
                    .isAuthorized
            }
        }
            .onSuccess { isAuthorized ->
                logger.info { "Retrieved response from authorization service (isAuthorized: $isAuthorized)" }
            }
            .onFailure { throwable ->
                logger.warn(throwable) { "Occurred error while to check transfer authorization" }
            }
            .getOrElse { false }
    }
}
