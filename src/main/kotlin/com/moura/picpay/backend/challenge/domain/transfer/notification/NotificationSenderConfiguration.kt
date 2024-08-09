package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.configuration.tracing.RequestTracingLoggingInterceptor
import com.moura.picpay.backend.challenge.configuration.tracing.ResponseTracingLoggingInterceptor
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.Executors

@Configuration
class NotificationSenderConfiguration {
    @Bean
    fun notificationSender(
        properties: NotificationSenderProperties,
        webClientBuilder: WebClient.Builder,
        notificationMetricsModule: NotificationMetricsModule,
    ): NotificationSender {
        return MockNotificationSender(
            webClient =
                webClientBuilder
                    .baseUrl(properties.baseUrl)
                    .filter(RequestTracingLoggingInterceptor)
                    .filter(ResponseTracingLoggingInterceptor)
                    .build(),
            metricsModule = notificationMetricsModule,
        )
    }

    @Bean
    fun notificationScope(): CoroutineScope {
        return CoroutineScope(
            Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher() +
                CoroutineName("notifications") +
                CoroutineExceptionHandler { _, throwable ->
                    val logger = KotlinLogging.logger(javaClass.packageName + ".CoroutineExceptionHandler")
                    logger.error(throwable) { "Uncaught exception in notifications scope (message: ${throwable.message})" }
                },
        )
    }
}
