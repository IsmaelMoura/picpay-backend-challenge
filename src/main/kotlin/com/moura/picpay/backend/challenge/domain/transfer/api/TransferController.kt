package com.moura.picpay.backend.challenge.domain.transfer.api

import com.moura.picpay.backend.challenge.domain.mappings.V1_TRANSFER_PATH
import com.moura.picpay.backend.challenge.domain.transfer.TransferService
import com.moura.picpay.backend.challenge.domain.transfer.api.validation.TransferValidator
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(V1_TRANSFER_PATH)
class TransferController(
    private val transferValidator: TransferValidator,
    private val transferService: TransferService,
) {

    @PostMapping
    suspend fun sendTransfer(@RequestBody transfer: TransferRequest) {
        logger.info { "Received transfer request (payer: ${transfer.payer}, payee: ${transfer.payee})" }

        transferValidator
            .validate(transfer)
            .let { transferService.transfer(transfer) }
            .let { ResponseEntity.accepted() }
    }
}