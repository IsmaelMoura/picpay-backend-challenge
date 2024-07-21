package com.moura.picpay.backend.challenge.domain.user

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class UserId(val value: Long) : Serializable {

    @Component
    object ToLongConverter : Converter<UserId, Long> {
        override fun convert(source: UserId): Long {
            return source.value
        }
    }

    @Component
    object FromLongConverter : Converter<Long, UserId> {
        override fun convert(source: Long): UserId {
            return UserId(source)
        }
    }
}