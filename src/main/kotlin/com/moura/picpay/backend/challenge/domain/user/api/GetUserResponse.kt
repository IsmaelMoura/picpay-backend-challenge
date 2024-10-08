package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserType
import org.jetbrains.annotations.TestOnly
import java.io.Serializable
import java.math.BigDecimal

data class GetUserResponse(
    val id: UserId,
    val countrySpecificId: CountrySpecificId,
    val fullName: String,
    val email: String,
    val type: UserType,
    val balance: BigDecimal,
) : Serializable {
    @TestOnly
    companion object
}
