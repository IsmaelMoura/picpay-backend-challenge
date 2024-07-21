package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.mappings.V1_USERS_PATH
import com.moura.picpay.backend.challenge.domain.user.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
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
    suspend fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<Unit> {
        return userService
            .also { logger.info { "Received create user request (userType: [${request.type}])" } }
            .createUser(request)
            .run {
                ResponseEntity
                    .created(URI.create("$V1_USERS_PATH/${id.value}"))
                    .build()
            }
    }
}