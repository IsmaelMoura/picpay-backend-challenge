package com.moura.picpay.backend.challenge.domain.user.api

import com.moura.picpay.backend.challenge.domain.exception.PicPayException
import com.moura.picpay.backend.challenge.domain.mappings.V1_USERS_PATH
import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.User
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserService
import com.moura.picpay.backend.challenge.domain.user.create
import com.moura.picpay.backend.challenge.domain.user.createFrom
import com.moura.picpay.backend.challenge.domain.user.createList
import com.moura.picpay.backend.challenge.domain.user.random
import com.moura.picpay.backend.challenge.utils.returnListBody
import com.moura.picpay.backend.challenge.utils.returnSingleBody
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.util.UriBuilder
import java.net.URI

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
                .returnSingleBody<GetUserResponse>() shouldBeEqual GetUserResponse.createFrom(user)
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
    fun `should get all user successfully`() =
        runTest {
            val users = User.createList()
            val request = FetchUsersQueryParametersRequest.createFrom(users)

            coEvery { userService.getAllUsers(request) } returns users.asFlow()

            webClient.get()
                .uri { builder -> builder.buildFetchUsers(request) }
                .exchange()
                .expectStatus().isOk
                .returnListBody<GetUserResponse>()
                .shouldContainExactlyInAnyOrder(users.map(GetUserResponse::createFrom))
        }

    @Test
    fun `should return empty list when there is no user existent`() =
        runTest {
            val request = FetchUsersQueryParametersRequest.create()
            coEvery { userService.getAllUsers(request) } returns emptyFlow()

            webClient.get()
                .uri { builder -> builder.buildFetchUsers(request) }
                .exchange()
                .expectStatus().isOk
                .returnListBody<GetUserResponse>()
                .shouldBeEmpty()
        }

    private fun UriBuilder.buildFetchUsers(request: FetchUsersQueryParametersRequest): URI {
        return path(V1_USERS_PATH)
            .queryParam(
                FetchUsersQueryParametersRequest::countrySpecificIds.name,
                request.countrySpecificIds?.map(CountrySpecificId::value),
            )
            .queryParam(FetchUsersQueryParametersRequest::fullNames.name, request.fullNames)
            .queryParam(FetchUsersQueryParametersRequest::emails.name, request.emails)
            .queryParam(FetchUsersQueryParametersRequest::type.name, request.type)
            .build()
    }
}
