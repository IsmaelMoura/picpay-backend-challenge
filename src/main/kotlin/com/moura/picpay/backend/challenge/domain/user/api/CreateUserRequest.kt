package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserType
import org.jetbrains.annotations.TestOnly
import java.io.Serializable
import java.math.BigDecimal

data class CreateUserRequest(
    val countrySpecificId: CountrySpecificId,
    val fullName: String,
    val email: String,
    val password: String,
    val type: UserType,
    val balance: BigDecimal = BigDecimal.ZERO,
) : Serializable {
    @TestOnly
    companion object
}
