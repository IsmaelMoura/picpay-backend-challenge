package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.mappings.V1_USERS_PATH
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserService
import com.moura.picpay.backend.challenge.domain.user.create
import com.moura.picpay.backend.challenge.domain.user.createFrom
import com.moura.picpay.backend.challenge.domain.user.createList
import com.moura.picpay.backend.challenge.domain.user.random
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest(UserController::class)
@ExtendWith(MockKExtension::class)
class UserControllerTest {
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
    fun `should get user by id successfully`() =
        runTest {
            val userId = UserId.random()
            val user = User.create(id = userId)

            coEvery { userService.getById(userId) } returns user

            webClient.get()
                .uri(V1_USERS_PATH + "/${userId.value}")
                .exchange()
                .expectStatus().isOk
                .returnResult<GetUserResponse>().responseBody
                .asFlow().toList()
                .shouldHaveSingleElement(GetUserResponse.createFrom(user))
        }

    @Test
    fun `should get all user successfully`() =
        runTest {
            val users = User.createList()

            coEvery { userService.getAllUsers() } returns users.asFlow()

            webClient.get()
                .uri(V1_USERS_PATH)
                .exchange()
                .expectStatus().isOk
                .returnResult<GetUserResponse>().responseBody
                .asFlow().toList()
                .shouldContainExactlyInAnyOrder(users.map(GetUserResponse::createFrom))
        }

    @Test
    fun `given user id not exist when get by id should return NOT_FOUND error`() {
        val userId = UserId.random()

        coEvery { userService.getById(userId) } throws PicPayException.UserNotFound(userId)

        webClient.get()
            .uri(V1_USERS_PATH + "/${userId.value}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return empty list when there is no user existent`() =
        runTest {
            coEvery { userService.getAllUsers() } returns emptyFlow()

            webClient.get()
                .uri(V1_USERS_PATH)
                .exchange()
                .expectStatus().isOk
                .returnResult<GetUserResponse>().responseBody
                .asFlow().toList()
                .shouldBeEmpty()
        }
}
