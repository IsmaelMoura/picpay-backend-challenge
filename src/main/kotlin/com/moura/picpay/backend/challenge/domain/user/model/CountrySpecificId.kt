package com.moura.picpay.backend.challenge.domain.user.model

import org.jetbrains.annotations.TestOnly
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class CountrySpecificId(
    val value: String,
) : Serializable,
    CharSequence by value {
    @Component
    @WritingConverter
    object ToStringConverter : Converter<CountrySpecificId, String> {
        override fun convert(source: CountrySpecificId): String = source.value
    }

    @Component
    @ReadingConverter
    object FromStringConverter : Converter<String, CountrySpecificId> {
        override fun convert(source: String): CountrySpecificId = CountrySpecificId(source)
    }

    @TestOnly
    companion object
}
