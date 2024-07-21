package com.moura.picpay.backend.challenge.domain.transfer.authorization

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class TransferAuthorizationConfiguration {
    @Bean
    fun transferAuthorizationService(properties: AuthorizationServiceProperties): TransferAuthorizationService {
        return MockTransferAuthorizationService(
            webClient = WebClient.create(properties.baseUrl),
        )
    }
}
