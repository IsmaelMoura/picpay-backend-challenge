package com.moura.picpay.backend.challenge.domain.transfer.notification

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
    fun notificationSender(properties: NotificationSenderProperties): NotificationSender {
        return MockNotificationSender(webClient = WebClient.create(properties.url))
    }

    @Bean
    fun notificationCoroutineScope(): CoroutineScope {
        return CoroutineScope(
            Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
                    + CoroutineName("notifications")
                    + CoroutineExceptionHandler { _, throwable ->
                val logger = KotlinLogging.logger(javaClass.packageName + ".CoroutineExceptionHandler")
                logger.error(throwable) { "Uncaught exception in notifications scope (message: ${throwable.message})" }
            }
        )
    }
}