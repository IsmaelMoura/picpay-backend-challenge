package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.notification.SendResult.Failure
import com.moura.picpay.backend.challenge.domain.transfer.notification.SendResult.Success

sealed interface SendResult {
    data object Success : SendResult

    data class Failure(val error: Throwable) : SendResult
}

inline fun sendCatching(block: () -> Unit): SendResult {
    return try {
        block()
        Success
    } catch (error: Throwable) {
        Failure(error)
    }
}

inline fun SendResult.onFailure(block: (error: Throwable) -> Unit): SendResult {
    return when (this) {
        is Failure -> apply { block(error) }
        is Success -> return this
    }
}

inline fun SendResult.onSuccess(block: () -> Unit): SendResult {
    return when (this) {
        is Failure -> return this
        is Success -> apply { block() }
    }
}
