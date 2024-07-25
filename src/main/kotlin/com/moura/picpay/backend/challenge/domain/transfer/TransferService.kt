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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val userService: UserService,
    private val authorizationService: TransferAuthorizationService,
    private val notificationSender: NotificationSender,
    private val notificationScope: CoroutineScope,
) {
    @Transactional
    suspend fun transfer(request: TransferRequest) {
        coroutineScope {
            val payer = async { userService.getById(request.payer) }
            val payee = async { userService.getById(request.payee) }

            payer.await().checkIsAllowedToTransfer(request)

            if (!authorizationService.isAuthorized()) {
                throw PicPayException.TransferAuthorization("Transfer was not authorized")
            }

            val updatedPayee = async { userService.updateUser(payee.await().withIncreasedBalance(request.value)) }
            val updatedPayer = async { userService.updateUser(payer.await().withDecreasedBalance(request.value)) }
            val transfer =
                createTransfer(
                    request = request,
                    payee = updatedPayee.await(),
                    payer = updatedPayer.await(),
                )

            notificationScope.launch {
                notificationSender.sendNotification(transfer)
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

    private final fun User.checkIsAllowedToTransfer(request: TransferRequest) {
        when {
            balance < request.value -> throw PicPayException.UserNotAllowedToTransfer(
                message = "User balance is not enough to transfer",
                userId = id,
            )

            type == UserType.MERCHANT -> throw PicPayException.UserNotAllowedToTransfer(
                message = "User type is ${UserType.MERCHANT} and isn't allowed to transfer",
                userId = id,
            )

            else -> {}
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
