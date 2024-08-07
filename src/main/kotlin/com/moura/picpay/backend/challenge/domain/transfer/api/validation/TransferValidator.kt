package com.moura.picpay.backend.challenge.domain.transfer.api.validation

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.exception.PicPayException.FieldViolation
import com.moura.picpay.backend.challenge.domain.transfer.api.TransferRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class TransferValidator {
    @Throws(PicPayException.TransferValidation::class)
    fun validate(request: TransferRequest): TransferRequest {
        return createViolations(request)
            .takeIf { it.isNotEmpty() }
            ?.let { throw PicPayException.TransferValidation(it) }
            ?: request
    }

    private fun createViolations(request: TransferRequest): Set<FieldViolation> {
        return buildSet {
            if (request.value < MIN_AMOUNT_VALUE) {
                add(FieldViolation(VALUE_FIELD_NAME, "Transfer value must be greater than 0"))
            }
        }
    }

    companion object {
        private val VALUE_FIELD_NAME: String = TransferRequest::value.name
        val MIN_AMOUNT_VALUE: BigDecimal = BigDecimal.valueOf(0.1)
    }
}
