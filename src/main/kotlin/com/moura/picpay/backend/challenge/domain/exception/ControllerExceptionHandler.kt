package com.moura.picpay.backend.challenge.domain.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(PicPayException::class)
    fun handlePicPayException(exception: PicPayException): ProblemDetail {
        logger.debug { "Handling ${exception::class.simpleName} exception (error: $exception)" }

        return exception.toProblemDetail()
    }
}
