package com.moura.picpay.backend.challenge.domain.masking

@JvmInline
value class SensitiveData<T>(val value: T) {
    override fun toString(): String {
        return "****"
    }
}

fun <T> T.asSensitiveData() = SensitiveData(this)
