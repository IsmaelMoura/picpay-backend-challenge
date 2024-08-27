package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.masking.SensitiveData
import com.moura.picpay.backend.challenge.domain.masking.asSensitiveData
import com.moura.picpay.backend.challenge.domain.user.model.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.model.UserType
import org.jetbrains.annotations.TestOnly
import java.io.Serializable
import java.math.BigDecimal

data class CreateUserRequest(
    val countrySpecificId: CountrySpecificId,
    val fullName: SensitiveData<String>,
    val email: SensitiveData<String>,
    val password: SensitiveData<String>,
    val type: UserType,
    val balance: SensitiveData<BigDecimal> = BigDecimal.ZERO.asSensitiveData(),
) : Serializable {
    @TestOnly
    companion object
}
