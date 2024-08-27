package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.masking.SensitiveData
import com.moura.picpay.backend.challenge.domain.masking.asSensitiveData
import com.moura.picpay.backend.challenge.domain.user.model.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.model.User
import com.moura.picpay.backend.challenge.domain.user.model.UserId
import com.moura.picpay.backend.challenge.domain.user.model.UserType
import com.moura.picpay.backend.challenge.domain.user.model.random
import com.moura.picpay.backend.challenge.utils.randomEmail
import com.moura.picpay.backend.challenge.utils.randomFullName
import java.math.BigDecimal
import kotlin.random.Random

fun GetUserResponse.Companion.create(
    id: UserId = UserId.random(),
    countrySpecificId: CountrySpecificId = CountrySpecificId.random(),
    fullName: SensitiveData<String> = String.randomFullName(),
    email: SensitiveData<String> = String.randomEmail(),
    type: UserType = UserType.entries.random(),
    balance: SensitiveData<BigDecimal> = BigDecimal.valueOf(Random.nextDouble()).asSensitiveData(),
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
