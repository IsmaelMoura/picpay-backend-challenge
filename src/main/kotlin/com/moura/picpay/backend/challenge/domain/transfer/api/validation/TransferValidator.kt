package com.moura.picpay.backend.challenge.domain.transfer.api.validation

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.exception.PicPayException.FieldViolation
import com.moura.picpay.backend.challenge.domain.transfer.api.TransferRequest
import java.math.BigDecimal

object TransferValidator {
    private const val VALUE_FIELD_NAME = "value"
    private val MIN_AMOUNT_VALUE = BigDecimal.valueOf(0.1)

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

        if (request.value < MIN_AMOUNT_VALUE) {
            violations.add(FieldViolation(VALUE_FIELD_NAME, "Transfer value must be greater than 0"))
        }

        return violations.toSet()
    }
}
