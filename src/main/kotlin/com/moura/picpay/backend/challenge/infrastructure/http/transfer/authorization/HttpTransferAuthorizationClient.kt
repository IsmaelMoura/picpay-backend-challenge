package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class HttpTransferAuthorizationClient(
    builder: WebClient.Builder,
    properties: AuthorizationClientProperties,
) : TransferAuthorizationClient {
    private val logger = KotlinLogging.logger {}
    private val webClient = builder.clone().baseUrl(properties.baseUrl).build()

    override suspend fun isAuthorized(): Boolean =
        runCatching {
            webClient
                .get()
                .retrieve()
                .awaitBody<AuthorizationResponse>()
                .isAuthorized
        }.onSuccess { isAuthorized ->
            logger.debug { "Retrieved response from authorization service (isAuthorized: $isAuthorized)" }
        }.onFailure { throwable ->
            logger.warn(throwable) { "Occurred error while to check transfer authorization" }
        }.getOrElse { false }
}
