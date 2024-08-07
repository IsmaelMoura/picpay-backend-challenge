package com.moura.picpay.backend.challenge.domain.transfer

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.transfer.api.TransferRequest
import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationService
import com.moura.picpay.backend.challenge.domain.transfer.notification.NotificationSender
import com.moura.picpay.backend.challenge.domain.transfer.persistence.TransferEntity
import com.moura.picpay.backend.challenge.domain.transfer.persistence.TransferRepository
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserService
import com.moura.picpay.backend.challenge.domain.user.UserType
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

private val logger = KotlinLogging.logger {}

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val userService: UserService,
    private val authorizationService: TransferAuthorizationService,
    private val transactional: TransactionalOperator,
    private val notificationSender: NotificationSender,
    private val notificationScope: CoroutineScope,
) {
    suspend fun transfer(request: TransferRequest): TransferId {
        return transactional.executeAndAwait {
            withContext(MDCContext()) {
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

                notificationScope.launch(MDCContext()) {
                    notificationSender.sendNotification(transfer)
                }

                transfer.id
            }
        }
    }

    private suspend fun createTransfer(
        request: TransferRequest,
        payee: User,
        payer: User,
    ): Transfer {
        return transferRepository
            .save(createTransferEntity(request))
            .toDomainTransfer(payee = payee, payer = payer)
            .also { logger.info { "Successfully created transfer [${it.id}] (payee: ${it.payee.id}, payer: ${it.payer.id})" } }
    }

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
                    message = "User type is ${UserType.MERCHANT} and isn't allowed to transfer",
                    userId = id,
                )
            }

            authorizationService.isAuthorized().not() -> {
                throw PicPayException.TransferAuthorization("Transfer was not authorized")
            }
        }
    }

    private fun createTransferEntity(request: TransferRequest): TransferEntity {
        return TransferEntity(
            payeeId = request.payee,
            payerId = request.payer,
            amount = request.value,
        )
    }

    private fun TransferEntity.toDomainTransfer(
        payee: User,
        payer: User,
    ): Transfer {
        return Transfer(
            id = id,
            payee = payee,
            payer = payer,
            amount = amount,
        )
    }
}
