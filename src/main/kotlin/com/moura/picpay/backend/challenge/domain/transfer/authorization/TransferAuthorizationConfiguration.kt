package com.moura.picpay.backend.challenge.domain.transfer.authorization

import com.moura.picpay.backend.challenge.configuration.tracing.RequestTracingLoggingInterceptor
import com.moura.picpay.backend.challenge.configuration.tracing.ResponseTracingLoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class TransferAuthorizationConfiguration {
    @Bean
    fun transferAuthorizationService(
        properties: AuthorizationServiceProperties,
        webClientBuilder: WebClient.Builder,
    ): TransferAuthorizationService {
        return MockTransferAuthorizationService(
            webClient =
                webClientBuilder
                    .baseUrl(properties.baseUrl)
                    .filter(RequestTracingLoggingInterceptor)
                    .filter(ResponseTracingLoggingInterceptor)
                    .build(),
        )
    }
}
