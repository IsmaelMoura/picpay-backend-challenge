package com.moura.picpay.backend.challenge.domain.transfer.model

import io.azam.ulidj.ULID
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class TransferId private constructor(val value: String) : Serializable {
    @Component
    @WritingConverter
    object ToStringConverter : Converter<TransferId, String> {
        override fun convert(source: TransferId): String {
            return source.value
        }
    }

    @Component
    @ReadingConverter
    object FromStringConverter : Converter<String, TransferId> {
        override fun convert(source: String): TransferId {
            return TransferId(source)
        }
    }

    companion object {
        fun random() = TransferId(ULID.random())
    }
}
