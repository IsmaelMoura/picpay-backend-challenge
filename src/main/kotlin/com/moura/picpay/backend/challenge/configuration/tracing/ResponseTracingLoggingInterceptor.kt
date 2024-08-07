package com.moura.picpay.backend.challenge.configuration.tracing

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

object ResponseTracingLoggingInterceptor : ExchangeFilterFunction {
    override fun filter(
        request: ClientRequest,
        next: ExchangeFunction,
    ): Mono<ClientResponse> {
        return next.exchange(request)
            .doOnNext { response ->
                logger.debug { "Received response from [${request.url()}] (headers: ${response.headers().asHttpHeaders()})" }
            }
    }
}
