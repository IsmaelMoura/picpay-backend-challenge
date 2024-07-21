package com.moura.picpay.backend.challenge.domain.user

import com.moura.picpay.backend.challenge.domain.user.api.CreateUserRequest
import io.azam.ulidj.ULID
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import java.math.BigDecimal
import kotlin.random.Random

fun CreateUserRequest.Companion.create(
    countrySpecificId: String = ULID.random(),
    fullName: String = randomAlphabetic(10, 20),
    email: String = randomAlphabetic(10, 20) + "@email.com",
    password: String = ULID.random(),
    type: UserType = UserType.entries.random(),
    balance: BigDecimal = BigDecimal.valueOf(Random.nextDouble()),
): CreateUserRequest {
    return CreateUserRequest(countrySpecificId, fullName, email, password, type, balance)
}

fun UserId.Companion.random() = UserId(Random.nextLong(1, Long.MAX_VALUE))
