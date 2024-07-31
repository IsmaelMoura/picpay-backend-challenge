package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserType
import org.jetbrains.annotations.TestOnly

data class FetchUsersQueryParametersRequest(
    val countrySpecificIds: List<CountrySpecificId>? = null,
    val fullNames: List<String>? = null,
    val emails: List<String>? = null,
    val type: UserType? = null,
) {
    @TestOnly
    companion object
}
