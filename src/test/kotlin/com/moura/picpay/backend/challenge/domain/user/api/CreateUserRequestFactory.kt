package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.masking.SensitiveData
import com.moura.picpay.backend.challenge.domain.masking.asSensitiveData
import com.moura.picpay.backend.challenge.domain.user.model.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.model.UserType
import com.moura.picpay.backend.challenge.domain.user.model.random
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import com.moura.picpay.backend.challenge.utils.randomPassword
import java.math.BigDecimal
import kotlin.random.Random

fun CreateUserRequest.Companion.create(
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: SensitiveData<String> = String.randomFullName(),
    email: SensitiveData<String> = String.randomEmail(),
    password: SensitiveData<String> = String.randomPassword(),
    type: UserType = UserType.entries.random(),
    balance: SensitiveData<BigDecimal> = BigDecimal.valueOf(Random.nextDouble()).asSensitiveData(),
): CreateUserRequest {
    return CreateUserRequest(countrySpecificId, fullName, email, password, type, balance)
}

fun CreateUserRequest.Companion.randomList(size: Int = Random.nextInt(10, 20)) = List(size) { create() }
