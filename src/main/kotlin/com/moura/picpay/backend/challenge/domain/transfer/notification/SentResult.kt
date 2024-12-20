package com.moura.picpay.backend.challenge.domain.transfer.notification

import com.moura.picpay.backend.challenge.domain.transfer.notification.SentResult.Failure
import com.moura.picpay.backend.challenge.domain.transfer.notification.SentResult.Success

sealed interface SentResult {
    data object Success : SentResult

    data class Failure(val error: Throwable) : SentResult
}

inline fun sendCatching(block: () -> Unit): SentResult {
    return try {
        block()
        Success
    } catch (error: Throwable) {
        Failure(error)
    }
}

inline fun SentResult.onFailure(block: (error: Throwable) -> Unit): SentResult {
    return when (this) {
        is Failure -> apply { block(error) }
        is Success -> return this
    }
}

inline fun SentResult.onSuccess(block: () -> Unit): SentResult {
    return when (this) {
        is Failure -> return this
        is Success -> apply { block() }
    }
}
