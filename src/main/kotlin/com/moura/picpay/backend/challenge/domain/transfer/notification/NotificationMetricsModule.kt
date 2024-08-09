package com.moura.picpay.backend.challenge.domain.transfer.notification

import io.micrometer.core.instrument.MeterRegistry
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
                timedValue.value
                    .onSuccess {
                        registry
                            .timer(NOTIFICATION_SENDING_TIMER)
                            .record(timedValue.duration.toJavaDuration())
                    }
            }
            .value
    }

    companion object {
        private const val NOTIFICATION_SENDING_TIMER = "transfer.notification.duration"
    }
}
