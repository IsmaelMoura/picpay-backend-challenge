package com.moura.picpay.backend.challenge.domain.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(PicPayException::class)
    fun handlePicPayException(exception: PicPayException): ProblemDetail {
        logger.warn(exception) { "Handling ${exception::class.simpleName} exception (message: ${exception.message})" }

        return exception.toProblemDetail()
    }
}
