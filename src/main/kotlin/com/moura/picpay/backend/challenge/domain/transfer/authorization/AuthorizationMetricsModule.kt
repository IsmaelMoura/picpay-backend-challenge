package com.moura.picpay.backend.challenge.domain.transfer.authorization

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
                    .timer("transfer.authorization.duration")
                    .record(it.duration.toJavaDuration())
            }
            .value
    }
}
