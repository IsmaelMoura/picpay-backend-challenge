package com.moura.picpay.backend.challenge.domain.transfer.api.validation

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.transfer.api.TransferRequest
import com.moura.picpay.backend.challenge.domain.transfer.api.create
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransferValidatorTest {
    private val underTest = TransferValidator()

    @Test
    fun `should return request when it is valid`() {
        val request: TransferRequest = TransferRequest.create()

        shouldNotThrowAny { underTest.validate(request) } shouldBeSameInstanceAs request
    }

    @Test
    fun `should throw exception when transfer request value is invalid`() {
        val request: TransferRequest = TransferRequest.create(value = BigDecimal.ZERO)

        shouldThrow<PicPayException.TransferValidation> { underTest.validate(request) }
    }
}
