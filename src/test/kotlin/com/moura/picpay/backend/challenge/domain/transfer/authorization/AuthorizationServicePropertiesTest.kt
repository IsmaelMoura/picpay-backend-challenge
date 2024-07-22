package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class AuthorizationServicePropertiesTest {
    @Test
    fun `baseUrl should be create as expected`() {
        val properties = AuthorizationServiceProperties("https://localhost:8080", "/api")

        properties.baseUrl shouldBeEqual "https://localhost:8080/api"
    }
}
