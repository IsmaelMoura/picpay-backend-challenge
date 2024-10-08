package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.mappings.V1_USERS_PATH
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping(V1_USERS_PATH)
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    suspend fun createUser(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<Unit> {
        return withContext(MDCContext()) {
            userService
                .also { logger.info { "Received create user request (userType: [${request.type}])" } }
                .createUser(request)
                .run {
                    ResponseEntity
                        .created(URI.create("$V1_USERS_PATH/${id.value}"))
                        .build()
                }
        }
    }

    @GetMapping("/{id}")
    suspend fun getUserById(
        @PathVariable id: UserId,
    ): ResponseEntity<GetUserResponse> {
        return withContext(MDCContext()) {
            userService.getById(id)
                .toGetUserResponse()
                .let { ResponseEntity.ok(it) }
        }
    }

    @GetMapping
    fun fetchUsers(request: FetchUsersQueryParametersRequest = FetchUsersQueryParametersRequest()): Flow<GetUserResponse> {
        return userService
            .getAllUsers(request)
            .map { it.toGetUserResponse() }
            .flowOn(MDCContext())
    }

    private fun User.toGetUserResponse(): GetUserResponse {
        return GetUserResponse(
            id = id,
            countrySpecificId = countrySpecificId,
            fullName = fullName,
            email = email,
            type = type,
            balance = balance,
        )
    }
}
