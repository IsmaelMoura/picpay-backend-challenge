package com.moura.picpay.backend.challenge.domain.transfer.notification

import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class NotificationSenderPropertiesTest {
    @Test
    fun `baseUrl should be created as expected`() {
        val properties = NotificationSenderProperties("https://localhost:8080", "/api")

        properties.baseUrl shouldBeEqual "https://localhost:8080/api"
    }
}
