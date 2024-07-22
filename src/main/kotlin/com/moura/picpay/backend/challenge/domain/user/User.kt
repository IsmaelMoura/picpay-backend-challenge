package com.moura.picpay.backend.challenge.domain.user

import org.jetbrains.annotations.TestOnly
import java.math.BigDecimal

data class User(
    val id: UserId,
    val countrySpecificId: CountrySpecificId,
    val fullName: String,
    val email: String,
    val password: String,
    val type: UserType,
    val balance: BigDecimal,
) {
    fun withIncreasedBalance(amount: BigDecimal): User {
        return copy(balance = balance + amount)
    }

    fun withDecreasedBalance(amount: BigDecimal): User {
        return copy(balance = balance - amount)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (countrySpecificId != other.countrySpecificId) return false
        if (fullName != other.fullName) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (type != other.type) return false
        if (balance.compareTo(other.balance) != 0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + countrySpecificId.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + balance.hashCode()
        return result
    }

    @TestOnly
    companion object
}
