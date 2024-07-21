package com.moura.picpay.backend.challenge.domain.transfer.api.validation

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.exception.PicPayException.TransferValidation.FieldViolation
import com.moura.picpay.backend.challenge.domain.transfer.api.TransferRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class TransferValidator {

    @Throws(PicPayException.TransferValidation::class)
    fun validate(request: TransferRequest): TransferRequest {
        val violations = createViolations(request)

        if (violations.isNotEmpty()) {
            throw PicPayException.TransferValidation(violations)
        }

        return request
    }

    private fun createViolations(request: TransferRequest): Set<FieldViolation> {
        val violations = mutableSetOf<FieldViolation>()

        if (request.value < MIN_VALUE_AMOUNT) {
            violations.add(FieldViolation(VALUE_FIELD_NAME, "Transfer value must be greater than 0"))
        }

        return violations.toSet()
    }

    companion object {
        private const val VALUE_FIELD_NAME = "value"

        private val MIN_VALUE_AMOUNT = BigDecimal.valueOf(0.1)
    }
}