package com.moura.picpay.backend.challenge.domain.transfer.executor.decorators

import com.moura.picpay.backend.challenge.domain.transfer.executor.TransferExecutor
import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.infrastructure.http.transfer.api.TransferRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

class TransactionalTransferExecutor(
    private val decorated: TransferExecutor,
    private val transaction: TransactionalOperator,
) : TransferExecutor {
    private val logger = KotlinLogging.logger {}

    override suspend fun execute(request: TransferRequest): Result<TransferId> =
        runCatching {
            transaction.executeAndAwait {
                decorated.execute(request).getOrThrow()
            }
        }.onSuccess { transferId ->
            logger.trace {
                "Successfully executed transfer within a transaction (" +
                    "request: $request, " +
                    "transferId: ${transferId.value})"
            }
        }.onFailure { cause ->
            logger.trace(cause) { "Failed to execute transfer within a transaction (request: $request)" }
        }
}

fun TransferExecutor.withTransaction(transaction: TransactionalOperator): TransactionalTransferExecutor =
    TransactionalTransferExecutor(this, transaction)
