package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.UserType
import java.io.Serializable
import java.math.BigDecimal

data class CreateUserRequest(
    val countrySpecificId: String,
    val fullName: String,
    val email: String,
    val password: String,
    val type: UserType,
    val balance: BigDecimal = BigDecimal.ZERO,
) : Serializable
