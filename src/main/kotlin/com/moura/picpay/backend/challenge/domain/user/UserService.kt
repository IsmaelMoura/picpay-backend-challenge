package com.moura.picpay.backend.challenge.domain.user

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.user.model.User
import com.moura.picpay.backend.challenge.domain.user.model.UserId
import com.moura.picpay.backend.challenge.domain.user.persistence.UserEntity
import com.moura.picpay.backend.challenge.domain.user.persistence.UserRepository
import com.moura.picpay.backend.challenge.infrastructure.http.user.api.CreateUserRequest
import com.moura.picpay.backend.challenge.infrastructure.http.user.api.FetchUsersQueryParametersRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun createUser(request: CreateUserRequest): User =
        userRepository
            .save(request.toEntity())
            .toDomainUser()
            .also { logger.info { "User [${it.id}] successfully created" } }

    suspend fun getById(id: UserId): User =
        userRepository
            .findById(id)
            ?.toDomainUser()
            ?.also { logger.info { "User [${it.id}] found on database" } }
            ?: throw PicPayException.UserNotFound(id)

    suspend fun updateUser(user: User): User =
        userRepository
            .findById(user.id)!!
            .copy(
                countrySpecificId = user.countrySpecificId,
                email = user.email,
                fullName = user.fullName,
                password = user.password,
                type = user.type,
                balance = user.balance,
            ).let { userRepository.save(it) }
            .toDomainUser()
            .also { logger.info { "$user successfully updated" } }

    fun getAllUsers(request: FetchUsersQueryParametersRequest): Flow<User> =
        userRepository
            .fetchAllUsers(request)
            .onEach { user ->
                logger.debug { "Found user [${user.id}] on database" }
            }.map { it.toDomainUser() }

    private fun CreateUserRequest.toEntity(): UserEntity =
        UserEntity(
            countrySpecificId = countrySpecificId,
            email = email,
            fullName = fullName,
            password = password,
            type = type,
            balance = balance,
        )

    private fun UserEntity.toDomainUser(): User =
        User(
            id = checkNotNull(id) { "UserId returned null from database" },
            countrySpecificId = countrySpecificId,
            email = email,
            fullName = fullName,
            password = password,
            type = type,
            balance = balance,
        )
}
