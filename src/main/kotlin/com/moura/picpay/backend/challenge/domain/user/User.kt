package com.moura.picpay.backend.challenge.domain.user

import java.math.BigDecimal

data class User(
    val id: UserId,
    val countrySpecificId: String,
    val fullName: String,
    val email: String,
    val password: String,
    val type: UserType,
    val balance: BigDecimal,
) {

    fun increaseBalance(amount: BigDecimal): User {
       return copy(balance = balance + amount)
    }

    fun decreaseBalance(amount: BigDecimal): User {
        return copy(balance = balance - amount)
    }
}