package com.moura.picpay.backend.challenge.domain.transfer.notification

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

@Component
class NotificationMetricsModule(
    private val registry: MeterRegistry,
) {
    suspend fun measureSendNotificationRequest(block: suspend () -> SendResult): SendResult {
        return measureTimedValue { block() }
            .also { timedValue ->
                Timer.builder(NOTIFICATION_SENDING_TIMER)
                    .description("Duration of send transfer notification")
                    .tags(
                        setOf(
                            Tag.of(SEND_FAILED_TAG, (timedValue.value is SendResult.Failure).toString()),
                        ),
                    )
                    .register(registry)
                    .record(timedValue.duration.toJavaDuration())
            }
            .value
    }

    companion object {
        private const val NOTIFICATION_SENDING_TIMER = "transfer.notification.duration"
        private const val SEND_FAILED_TAG = "failed"
    }
}
