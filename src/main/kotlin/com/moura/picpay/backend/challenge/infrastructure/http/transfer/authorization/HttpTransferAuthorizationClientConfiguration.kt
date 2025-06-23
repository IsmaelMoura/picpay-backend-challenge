package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import com.moura.picpay.backend.challenge.domain.transfer.authorization.decorators.decorateWithMetrics
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class HttpTransferAuthorizationClientConfiguration {
    @Bean
    fun transferAuthorizationClient(
        webClientBuilder: WebClient.Builder,
        authorizationClientProperties: AuthorizationClientProperties,
        meterRegistry: MeterRegistry,
    ): TransferAuthorizationClient =
        HttpTransferAuthorizationClient(webClientBuilder, authorizationClientProperties)
            .decorateWithMetrics(meterRegistry)
}
