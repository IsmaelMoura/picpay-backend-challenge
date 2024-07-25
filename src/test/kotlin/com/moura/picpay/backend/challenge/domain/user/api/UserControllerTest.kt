package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.ApiIntegrationTest
import com.moura.picpay.backend.challenge.domain.mappings.V1_USERS_PATH
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserService
import com.moura.picpay.backend.challenge.domain.user.create
import com.moura.picpay.backend.challenge.domain.user.createFrom
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest(UserController::class)
class UserControllerTest : ApiIntegrationTest() {
    @Autowired
    private lateinit var webClient: WebTestClient

    @field:MockkBean
    private lateinit var userService: UserService

    @Test
    fun `should create user successfully`() {
        val request = CreateUserRequest.create()
        val user = User.createFrom(request)

        coEvery { userService.createUser(request) } returns user

        webClient.post()
            .uri(V1_USERS_PATH)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().location(V1_USERS_PATH + "/${user.id.value}")

        coVerify { userService.createUser(request) }
    }

    @Test
    @Disabled
    fun `should get user by id successfully`() {
    }

    @Test
    @Disabled
    fun `should get all user successfully`() {
    }

    @Test
    @Disabled
    fun `given user id not exist when get by id should return NOT_FOUND error`() {
    }

    @Test
    @Disabled
    fun `should return empty when there is no user existent`() {
    }
}
