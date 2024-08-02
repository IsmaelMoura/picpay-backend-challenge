package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserType
import com.moura.picpay.backend.challenge.domain.user.random
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import io.azam.ulidj.ULID
import java.math.BigDecimal
import kotlin.random.Random

fun CreateUserRequest.Companion.create(
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: String = String.randomFullName(),
    email: String = String.randomEmail(),
    password: String = ULID.random(),
    type: UserType = UserType.entries.random(),
    balance: BigDecimal = BigDecimal.valueOf(Random.nextDouble()),
): CreateUserRequest {
    return CreateUserRequest(countrySpecificId, fullName, email, password, type, balance)
}

fun CreateUserRequest.Companion.randomList(size: Int = Random.nextInt(10, 20)) = List(size) { create() }
