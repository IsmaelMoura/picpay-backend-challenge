package com.moura.picpay.backend.challenge.domain.transfer.api

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.mappings.V1_TRANSFER_PATH
import com.moura.picpay.backend.challenge.domain.transfer.TransferId
import com.moura.picpay.backend.challenge.domain.transfer.TransferService
import com.moura.picpay.backend.challenge.domain.transfer.api.validation.TransferValidator
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.random
import com.moura.picpay.backend.challenge.utils.returnSingleBody
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest(TransferController::class)
@ExtendWith(MockKExtension::class)
class TransferControllerTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @field:SpykBean
    private lateinit var transferValidator: TransferValidator

    @field:MockkBean
    private lateinit var transferService: TransferService

    @Test
    fun `should create transfer successfully`() =
        runTest {
            val request = TransferRequest.create()
            val transferId = TransferId.random()

            coEvery { transferService.transfer(request) } returns transferId

            webClient.post()
                .uri(V1_TRANSFER_PATH)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isAccepted
                .returnSingleBody<TransferResponse>() shouldBeEqual TransferResponse(transferId)

            coVerify { transferValidator.validate(request) }
            coVerify { transferService.transfer(request) }
        }

    @Test
    fun `should return BAD_REQUEST when transfer value is less than minimum accepted`() {
        val request = TransferRequest.create(value = TransferValidator.MIN_AMOUNT_VALUE - 0.0000001.toBigDecimal())

        webClient
            .post()
            .uri(V1_TRANSFER_PATH)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest

        coVerify { transferValidator.validate(request) }
        coVerify(exactly = 0) { transferService.transfer(request) }
    }

    @Test
    fun `should return BAD_REQUEST when service throws UserNotAllowedToTransfer`() {
        val request = TransferRequest.create()

        coEvery { transferService.transfer(request) } throws PicPayException.UserNotAllowedToTransfer("Invalid user", UserId.random())

        webClient.post()
            .uri(V1_TRANSFER_PATH)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest

        coVerify { transferValidator.validate(request) }
        coVerify { transferService.transfer(request) }
    }

    @Test
    fun `should return FORBIDDEN when service throw TransferAuthorization exception`() {
        val request = TransferRequest.create()

        coEvery { transferService.transfer(request) } throws PicPayException.TransferAuthorization("Transfer was not authorized")

        webClient.post()
            .uri(V1_TRANSFER_PATH)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isForbidden

        coEvery { transferService.transfer(request) }
        coEvery { transferValidator.validate(request) }
    }
}
