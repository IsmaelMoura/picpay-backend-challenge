package com.moura.picpay.backend.challenge.domain.transfer.authorization

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

@Component
class AuthorizationMetricsModule(
    private val registry: MeterRegistry,
) {
    suspend fun measureGetAuthorizationData(block: suspend () -> Boolean): Boolean {
        return measureTimedValue { block() }
            .also { timedValue ->
                Timer
                    .builder(AUTHORIZATION_TIMER)
                    .description("Duration of authorization requests")
                    .tags(
                        setOf(
                            Tag.of("authorized", timedValue.value.toString()),
                        ),
                    )
                    .register(registry)
                    .record(timedValue.duration.toJavaDuration())
            }.value
    }

    companion object {
        private const val AUTHORIZATION_TIMER = "transfer.authorization.duration"
    }
}
