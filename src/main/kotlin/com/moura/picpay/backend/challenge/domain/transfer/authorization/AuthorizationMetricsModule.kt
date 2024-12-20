package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

@Component
class AuthorizationMetricsModule(
    private val meterRegistry: MeterRegistry,
) {
    suspend fun measureGetAuthorizationData(block: suspend () -> Boolean): Boolean {
        return measureTimedValue { block() }
            .also { (isAuthorized, elapsedTime) ->
                Timer
                    .builder(AUTHORIZATION_TIMER)
                    .description("Duration of authorization requests")
                    .tags(
                        setOf(
                            Tag.of(IS_AUTHORIZED, isAuthorized.toString()),
                        ),
                    )
                    .register(meterRegistry)
                    .record(elapsedTime.toJavaDuration())
            }.value
    }

    companion object {
        const val AUTHORIZATION_TIMER = "transfer.authorization"
        const val IS_AUTHORIZED = "is_authorized"
    }
}
