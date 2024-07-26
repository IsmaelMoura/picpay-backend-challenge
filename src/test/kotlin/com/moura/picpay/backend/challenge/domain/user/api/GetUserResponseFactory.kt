package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserType
import com.moura.picpay.backend.challenge.domain.user.random
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import java.math.BigDecimal
import kotlin.random.Random

fun GetUserResponse.Companion.create(
    id: UserId = UserId.random(),
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: String = String.randomFullName(),
    email: String = String.randomEmail(),
    type: UserType = UserType.entries.random(),
    balance: BigDecimal = BigDecimal.valueOf(Random.nextDouble()),
): GetUserResponse {
    return GetUserResponse(id, countrySpecificId, fullName, email, type, balance)
}

fun GetUserResponse.Companion.createFrom(user: User): GetUserResponse {
    return with(user) {
        create(
            id = id,
            countrySpecificId = countrySpecificId,
            fullName = fullName,
            email = email,
            type = type,
            balance = balance,
        )
    }
}
