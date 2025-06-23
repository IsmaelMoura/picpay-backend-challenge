package com.moura.picpay.backend.challenge.domain.user.model

import com.moura.picpay.backend.challenge.infrastructure.http.user.api.CreateUserRequest
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import com.moura.picpay.backend.challenge.utils.randomPassword
import java.math.BigDecimal
import kotlin.random.Random

fun User.Companion.create(
    id: UserId = UserId.random(),
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: String = String.randomFullName(),
    email: String = String.randomEmail(),
    password: String = String.randomPassword(),
    type: UserType = UserType.entries.random(),
    balance: BigDecimal = BigDecimal.valueOf(Random.nextDouble()),
): User =
    User(
        id = id,
        countrySpecificId = countrySpecificId,
        fullName = fullName,
        email = email,
        password = password,
        type = type,
        balance = balance,
    )

fun User.Companion.createFrom(request: CreateUserRequest): User =
    with(request) {
        create(
            countrySpecificId = countrySpecificId,
            fullName = fullName,
            email = email,
            password = password,
            type = type,
        )
    }

fun User.Companion.createList(size: Int = Random.nextInt(10, 20)) = List(size) { create() }
