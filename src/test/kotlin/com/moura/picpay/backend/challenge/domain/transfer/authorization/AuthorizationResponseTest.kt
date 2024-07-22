package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AuthorizationResponseTest {
    @Test
    fun `isAuthorized should be as expected`() {
        val authorized = AuthorizationResponse(AuthorizationResponse.Data(authorization = true))
        val unauthorized = AuthorizationResponse(AuthorizationResponse.Data(authorization = false))

        authorized.isAuthorized shouldBe true
        unauthorized.isAuthorized shouldBe false
    }
}
