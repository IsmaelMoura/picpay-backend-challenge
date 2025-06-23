package com.moura.picpay.backend.challenge.domain.transfer.authorization.decorators

import com.moura.picpay.backend.challenge.domain.transfer.authorization.TransferAuthorizationClient
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

class TimeMeasuredTransferAuthorizationClient(
    private val decorated: TransferAuthorizationClient,
    private val meterRegistry: MeterRegistry,
) : TransferAuthorizationClient {
    override suspend fun isAuthorized(): Boolean =
        measureTimedValue { decorated.isAuthorized() }
            .apply {
                meterRegistry
                    .timer(
                        TRANSFER_AUTHORIZATION_TIMER,
                        Tags.of(
                            Tag.of(IS_AUTHORIZED_TAG, value.toString()),
                        ),
                    ).record(duration.toJavaDuration())
            }.value

    companion object {
        const val TRANSFER_AUTHORIZATION_TIMER = "transfer.authorization"

        const val IS_AUTHORIZED_TAG = "is.authorized"
    }
}

fun TransferAuthorizationClient.decorateWithMetrics(meterRegistry: MeterRegistry) =
    TimeMeasuredTransferAuthorizationClient(this, meterRegistry)
