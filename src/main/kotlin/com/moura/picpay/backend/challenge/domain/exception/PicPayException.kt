package com.moura.picpay.backend.challenge.domain.exception

import com.moura.picpay.backend.challenge.domain.user.model.UserId
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

sealed class PicPayException(
    override val message: String,
    private val status: HttpStatus,
) : RuntimeException(message) {
    fun toProblemDetail(): ProblemDetail = ProblemDetail.forStatusAndDetail(status, message).apply { setSpecificInfo() }

    protected abstract fun ProblemDetail.setSpecificInfo()

    data class FieldViolation(
        val field: String,
        val description: String,
    )

    class TransferValidation(
        private val errors: Set<FieldViolation>,
    ) : PicPayException("Transfer request is invalid.", HttpStatus.BAD_REQUEST) {
        override fun ProblemDetail.setSpecificInfo() {
            setProperty("field_violations", errors)
        }
    }

    class TransferAuthorization : PicPayException("Transfer unauthorized", HttpStatus.FORBIDDEN) {
        override fun ProblemDetail.setSpecificInfo() {}
    }

    class UserNotAllowedToTransfer(
        message: String,
        private val userId: UserId,
    ) : PicPayException(message, HttpStatus.BAD_REQUEST) {
        override fun ProblemDetail.setSpecificInfo() {
            setProperty("user_id", userId)
        }
    }

    class UserNotFound(
        private val userId: UserId,
    ) : PicPayException("User does not exist.", HttpStatus.NOT_FOUND) {
        override fun ProblemDetail.setSpecificInfo() {
            setProperty("user_id", userId)
        }
    }
}
