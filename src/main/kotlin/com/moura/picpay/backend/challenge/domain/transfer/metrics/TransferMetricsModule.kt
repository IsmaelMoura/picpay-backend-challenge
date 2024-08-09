package com.moura.picpay.backend.challenge.domain.transfer.metrics

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.time.measureTimedValue

@Component
class TransferMetricsModule(
    private val registry: MeterRegistry,
) {
    final suspend fun <T> measureTransferOperation(block: suspend () -> T): T {
        return measureTimedValue {
            block()
        }.also {
            registry.timer(TRANSFER_OPERATION_TIMER).record(it.duration.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        }.value
    }

    companion object {
        private const val TRANSFER_OPERATION_TIMER = "transfer.operation.duration"
    }
}
