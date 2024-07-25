package com.moura.picpay.backend.challenge.domain.user

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.random.Random

class UserTest {
    @Test
    fun withIncreasedBalance() {
        // given
        val user = User.create()
        val amountToIncrease = BigDecimal.valueOf(Random.nextDouble())

        val increased = user.withIncreasedBalance(amountToIncrease)

        increased.balance shouldBe user.balance + amountToIncrease
    }

    @Test
    fun withDecreasedBalance() {
        // given
        val user = User.create()
        val amountToDecrease = BigDecimal.valueOf(Random.nextDouble())

        val increased = user.withDecreasedBalance(amountToDecrease)

        increased.balance shouldBe user.balance - amountToDecrease
    }
}
