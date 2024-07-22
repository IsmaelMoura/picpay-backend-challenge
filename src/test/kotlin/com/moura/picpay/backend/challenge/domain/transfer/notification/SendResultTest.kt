package com.moura.picpay.backend.challenge.domain.transfer.notification

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.junit.jupiter.api.Test

class SendResultTest {
    @Test
    fun `should return success when exception no thrown`() {
        val result = sendCatching { }

        result shouldBe instanceOf<SendResult.Success>()
    }

    @Test
    fun `should return failure when exception thrown`() {
        val result = sendCatching { throw Exception() }

        result shouldBe instanceOf<SendResult.Failure>()
    }

    @Test
    fun `should execute onSuccess block when is Success instance`() {
        var counter: Int? = null

        sendCatching { }
            .onSuccess { counter = 1 }
            .onFailure { counter = 2 }

        counter shouldBe 1
    }

    @Test
    fun `should execute onFailure block when is Failure instance`() {
        var counter: Int? = null

        sendCatching { throw Exception() }
            .onSuccess { counter = 1 }
            .onFailure { counter = 2 }

        counter shouldBe 2
    }
}
