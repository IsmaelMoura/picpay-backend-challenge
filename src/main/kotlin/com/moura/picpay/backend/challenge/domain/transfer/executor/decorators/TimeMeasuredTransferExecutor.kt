package com.moura.picpay.backend.challenge.domain.transfer.executor.decorators

import com.moura.picpay.backend.challenge.domain.transfer.executor.TransferExecutor
import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.infrastructure.http.transfer.api.TransferRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

class TimeMeasuredTransferExecutor(
    private val decorated: TransferExecutor,
    private val meterRegistry: MeterRegistry,
) : TransferExecutor {
    private val logger = KotlinLogging.logger {}

    override suspend fun execute(request: TransferRequest): Result<TransferId> {
        return measureTimedValue { decorated.execute(request) }
            .apply {
                value
                    .onSuccess { transferId ->
                        meterRegistry.timer(TRANSFER_EXECUTION_SUCCEED_TIMER).record(duration.toJavaDuration())
                        logger.trace {
                            "Recorded successful transfer execution result (" +
                                "request: $request, " +
                                "transferId: ${transferId.value}, " +
                                "duration: $duration)"
                        }
                    }
                    .onFailure { cause ->
                        meterRegistry.timer(TRANSFER_EXECUTION_FAILED_TIMER).record(duration.toJavaDuration())
                        logger.trace(cause) {
                            "Recorded failure transfer execution result (request: $request, duration: $duration)"
                        }
                    }
            }
            .value
    }

    companion object {
        const val TRANSFER_EXECUTION_SUCCEED_TIMER = "transfer.execute.success.timer"
        const val TRANSFER_EXECUTION_FAILED_TIMER = "transfer.execute.fail.timer"
    }
}

fun TransferExecutor.withTimeMeasured(meterRegistry: MeterRegistry): TransferExecutor {
    return TimeMeasuredTransferExecutor(this, meterRegistry)
}
