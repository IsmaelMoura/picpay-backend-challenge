package com.moura.picpay.backend.challenge.infrastructure.http.transfer.authorization

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class AuthorizationClientPropertiesTest {
    @Test
    fun `baseUrl should be create as expected`() {
        val host = "https://localhost:8080"
        val path = "api/v1/authorization"
        val properties = AuthorizationClientProperties(host, path)

        properties.baseUrl shouldBeEqual "$host/$path"
    }
}
