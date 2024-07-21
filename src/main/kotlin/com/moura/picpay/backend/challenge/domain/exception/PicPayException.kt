package com.moura.picpay.backend.challenge.domain.exception

import com.moura.picpay.backend.challenge.domain.user.UserId
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

sealed class PicPayException(
    override val message: String,
    private val status: HttpStatus
) : RuntimeException(message) {

    open fun toProblemDetail(): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(status, message)
    }

    class TransferValidation(
        private val errors: Set<FieldViolation>
    ) : PicPayException("Transfer request is invalid.", HttpStatus.BAD_REQUEST) {

        data class FieldViolation(val field: String, val description: String)

        override fun toProblemDetail(): ProblemDetail {
            return super.toProblemDetail()
                .apply {
                    setProperty("field_violations", errors)
                }
        }
    }

    class TransferAuthorization(message: String) : PicPayException(message, HttpStatus.UNAUTHORIZED)

    class UserNotAllowedToTransfer(
        message: String,
        private val userId: UserId
    ) : PicPayException(message, HttpStatus.BAD_REQUEST) {

        override fun toProblemDetail(): ProblemDetail {
            return super.toProblemDetail().apply { setProperty("user_id", userId) }
        }
    }

    class UserNotFound(private val userId: UserId) : PicPayException("User does not exist.", HttpStatus.NOT_FOUND) {

        override fun toProblemDetail(): ProblemDetail {
            return super.toProblemDetail().apply { setProperty("user_id", userId) }
        }
    }
}