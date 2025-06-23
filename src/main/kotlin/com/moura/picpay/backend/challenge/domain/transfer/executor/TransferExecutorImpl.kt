package com.moura.picpay.backend.challenge.domain.transfer.executor

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import com.moura.picpay.backend.challenge.domain.transfer.model.Transfer
import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.domain.transfer.notification.TransferNotificationSender
import com.moura.picpay.backend.challenge.domain.transfer.persistence.TransferEntity
import com.moura.picpay.backend.challenge.domain.transfer.persistence.TransferRepository
import com.moura.picpay.backend.challenge.domain.user.UserService
import com.moura.picpay.backend.challenge.domain.user.model.User
import com.moura.picpay.backend.challenge.domain.user.model.UserType
import com.moura.picpay.backend.challenge.infrastructure.http.transfer.api.TransferRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TransferExecutorImpl(
    private val transferRepository: TransferRepository,
    private val userService: UserService,
    private val authorizationClient: TransferAuthorizationClient,
    private val transferNotificationSender: TransferNotificationSender,
) : TransferExecutor {
    private val logger = KotlinLogging.logger {}

    override suspend fun execute(request: TransferRequest): Result<TransferId> =
        runCatching {
            coroutineScope {
                val payer = async { userService.getById(request.payer) }
                val payee = async { userService.getById(request.payee) }

                payer.await().checkIsAllowedToTransfer(request)

                val updatedPayee = async { userService.updateUser(payee.await().withIncreasedBalance(request.value)) }
                val updatedPayer = async { userService.updateUser(payer.await().withDecreasedBalance(request.value)) }
                val transfer =
                    createTransfer(
                        request = request,
                        payee = updatedPayee.await(),
                        payer = updatedPayer.await(),
                    )

                transferNotificationSender.send(transfer)

                transfer.id
            }
        }.onSuccess {
            logger.info {
                "Successfully executed transfer (" +
                    "transferId: ${it.value}, " +
                    "payee: ${request.payee}, " +
                    "payer: ${request.payer})"
            }
        }.onFailure { cause ->
            logger.warn(cause) { "Failed to execute transfer (payee: ${request.payee}, payee: ${request.payer})" }
        }

    private suspend fun createTransfer(
        request: TransferRequest,
        payee: User,
        payer: User,
    ): Transfer =
        transferRepository
            .save(createTransferEntity(request))
            .toDomainTransfer(payee = payee, payer = payer)
            .also { logger.info { "Successfully created transfer [${it.id}] (payee: ${it.payee.id}, payer: ${it.payer.id})" } }

    private suspend fun User.checkIsAllowedToTransfer(request: TransferRequest) {
        when {
            balance < request.value -> {
                throw PicPayException.UserNotAllowedToTransfer(
                    message = "User balance is not enough to transfer",
                    userId = id,
                )
            }

            type == UserType.MERCHANT -> {
                throw PicPayException.UserNotAllowedToTransfer(
                    message = "User type is $type and isn't allowed to transfer",
                    userId = id,
                )
            }

            authorizationClient.isAuthorized().not() -> {
                throw PicPayException.TransferAuthorization()
            }
        }
    }

    private fun createTransferEntity(request: TransferRequest): TransferEntity =
        TransferEntity(
            payeeId = request.payee,
            payerId = request.payer,
            amount = request.value,
        )

    private fun TransferEntity.toDomainTransfer(
        payee: User,
        payer: User,
    ): Transfer =
        Transfer(
            id = id,
            payee = payee,
            payer = payer,
            amount = amount,
        )
}
