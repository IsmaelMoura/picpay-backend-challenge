package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

@Component
class AuthorizationMetricsModule(
    private val registry: MeterRegistry,
) {
    suspend fun measureGetAuthorizationData(block: suspend () -> Boolean): Boolean {
        return measureTimedValue { block() }
            .also {
                registry
                    .timer(AUTHORIZATION_TIMER)
                    .record(it.duration.toJavaDuration())
            }
            .value
    }

    fun incrementAuthorizationDataCount(authorized: Boolean) {
        createAuthorizationDataCounter(authorized).increment()
    }

    private fun createAuthorizationDataCounter(authorized: Boolean): Counter {
        return when (authorized) {
            true ->
                Counter
                    .builder(AUTHORIZED_COUNTER_NAME)
                    .description("Number of authorized transfers")
                    .register(registry)

            false ->
                Counter
                    .builder(UNAUTHORIZED_COUNTER_NAME)
                    .description("Number of unauthorized transfers")
                    .register(registry)
        }
    }

    companion object {
        private const val AUTHORIZATION_TIMER = "transfer.authorization.duration"
        private const val AUTHORIZED_COUNTER_NAME = "transfer.authorization.authorized"
        private const val UNAUTHORIZED_COUNTER_NAME = "transfer.authorization.unauthorized"
    }
}
