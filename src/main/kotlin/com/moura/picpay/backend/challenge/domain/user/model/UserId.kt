package com.moura.picpay.backend.challenge.domain.user.model

import org.jetbrains.annotations.TestOnly
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class UserId(
    val value: Long,
) : Serializable {
    @Component
    @WritingConverter
    object ToLongConverter : Converter<UserId, Long> {
        override fun convert(source: UserId): Long = source.value
    }

    @Component
    @ReadingConverter
    object FromLongConverter : Converter<Long, UserId> {
        override fun convert(source: Long): UserId = UserId(source)
    }

    @TestOnly
    companion object
}
