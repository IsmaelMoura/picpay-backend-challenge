package com.moura.picpay.backend.challenge.domain.user.persistence

import com.moura.picpay.backend.challenge.domain.user.api.FetchUsersQueryParametersRequest
import kotlinx.coroutines.flow.Flow

sealed interface CustomizedUserRepository {
    fun fetchAllUsers(request: FetchUsersQueryParametersRequest): Flow<UserEntity>
}
