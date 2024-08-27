package com.moura.picpay.backend.challenge.domain.user.model

import com.moura.picpay.backend.challenge.domain.masking.SensitiveData
import com.moura.picpay.backend.challenge.domain.masking.asSensitiveData
import com.moura.picpay.backend.challenge.domain.user.api.CreateUserRequest
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import com.moura.picpay.backend.challenge.utils.randomPassword
import java.math.BigDecimal
import kotlin.random.Random

fun User.Companion.create(
    id: UserId = UserId.random(),
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: SensitiveData<String> = String.randomFullName(),
    email: SensitiveData<String> = String.randomEmail(),
    password: SensitiveData<String> = String.randomPassword(),
    type: UserType = com.moura.picpay.backend.challenge.domain.user.model.UserType.entries.random(),
    balance: SensitiveData<BigDecimal> = BigDecimal.valueOf(Random.nextDouble()).asSensitiveData(),
): User {
    return User(
        id = id,
        countrySpecificId = countrySpecificId,
        fullName = fullName,
        email = email,
        password = password,
        type = type,
        balance = balance,
    )
}

fun User.Companion.createFrom(request: CreateUserRequest): User {
    return with(request) {
        create(
            countrySpecificId = countrySpecificId,
            fullName = fullName,
            email = email,
            password = password,
            type = type,
        )
    }
}

fun User.Companion.createList(size: Int = Random.nextInt(10, 20)) = List(size) { create() }
