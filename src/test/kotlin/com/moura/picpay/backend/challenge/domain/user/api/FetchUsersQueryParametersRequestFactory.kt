package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserType
import com.moura.picpay.backend.challenge.domain.user.randomList
import com.moura.picpay.backend.challenge.utils.randomEmailList
import com.moura.picpay.backend.challenge.utils.randomFullNameList

fun FetchUsersQueryParametersRequest.Companion.createFrom(
    users: Collection<User>,
    type: UserType? = null,
): FetchUsersQueryParametersRequest {
    return FetchUsersQueryParametersRequest(
        countrySpecificIds = users.map(User::countrySpecificId),
        fullNames = users.map(User::fullName),
        emails = users.map(User::email),
        type = type,
    )
}

fun FetchUsersQueryParametersRequest.Companion.create(
    countrySpecificIds: List<CountrySpecificId>? = CountrySpecificId.randomList(),
    fullNames: List<String>? = String.randomFullNameList(),
    emails: List<String>? = String.randomEmailList(),
    type: UserType? = UserType.entries.random(),
): FetchUsersQueryParametersRequest {
    return FetchUsersQueryParametersRequest(
        countrySpecificIds = countrySpecificIds,
        fullNames = fullNames,
        emails = emails,
        type = type
    )
}
