package com.moura.picpay.backend.challenge.domain.transfer.model

import io.azam.ulidj.ULID
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TransferIdTest {
    @Test
    fun `should generate id correctly`() {
        val id = TransferId.random()

        ULID.isValid(id.value) shouldBe true
    }
}
