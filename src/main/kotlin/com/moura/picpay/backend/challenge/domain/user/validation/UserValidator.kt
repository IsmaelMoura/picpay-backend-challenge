package com.moura.picpay.backend.challenge.domain.user.validation

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.exception.PicPayException.FieldViolation
import com.moura.picpay.backend.challenge.domain.money.MAX_SUPPORTED_AMOUNT_VALUE
import com.moura.picpay.backend.challenge.domain.user.api.CreateUserRequest
import org.springframework.stereotype.Component

@Component
class UserValidator {
    @Throws(PicPayException.CreateUserValidation::class)
    fun validate(request: CreateUserRequest): CreateUserRequest {
        val violations = createViolations(request)

        if (violations.isNotEmpty()) {
            throw PicPayException.CreateUserValidation(violations)
        }

        return request
    }

    private fun createViolations(request: CreateUserRequest): Set<FieldViolation> {
        val violations = mutableSetOf<FieldViolation>()

        if (request.balance > MAX_SUPPORTED_AMOUNT_VALUE) {
            violations.add(FieldViolation(BALANCE_FIELD_NAME, "User balance must be less than $MAX_SUPPORTED_AMOUNT_VALUE"))
        }

        return violations.toSet()
    }

    companion object {
        private const val BALANCE_FIELD_NAME = "balance"
    }
}
