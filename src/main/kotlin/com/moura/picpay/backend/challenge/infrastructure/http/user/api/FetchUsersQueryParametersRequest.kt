package com.moura.picpay.backend.challenge.infrastructure.http.user.api

import com.moura.picpay.backend.challenge.domain.user.model.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.model.UserType
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
