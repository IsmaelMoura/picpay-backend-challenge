package com.moura.picpay.backend.challenge.domain.transfer.executor

import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import com.moura.picpay.backend.challenge.domain.transfer.executor.decorators.withTimeMeasured
import com.moura.picpay.backend.challenge.domain.transfer.executor.decorators.withTransaction
import com.moura.picpay.backend.challenge.domain.transfer.notification.TransferNotificationSender
import com.moura.picpay.backend.challenge.domain.transfer.persistence.TransferRepository
import com.moura.picpay.backend.challenge.domain.user.UserService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class TransferExecutorConfig {
    @Bean
    fun transferExecutor(
        transferRepository: TransferRepository,
        userService: UserService,
        authorizationClient: TransferAuthorizationClient,
        transferNotificationSender: TransferNotificationSender,
        transactionalOperator: TransactionalOperator,
        meterRegistry: MeterRegistry,
    ): TransferExecutor {
        return TransferExecutorImpl(
            transferRepository = transferRepository,
            userService = userService,
            authorizationClient = authorizationClient,
            transferNotificationSender = transferNotificationSender,
        )
            .withTransaction(transactionalOperator)
            .withTimeMeasured(meterRegistry)
    }
}
