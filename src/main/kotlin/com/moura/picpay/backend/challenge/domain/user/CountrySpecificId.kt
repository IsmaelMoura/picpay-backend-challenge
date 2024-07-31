package com.moura.picpay.backend.challenge.domain.user

import org.jetbrains.annotations.TestOnly
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class CountrySpecificId(val value: String) : Serializable {
    @Component
    object ToStringConverter : Converter<CountrySpecificId, String> {
        override fun convert(source: CountrySpecificId): String {
            return source.value
        }
    }

    @Component
    object FromStringConverter : Converter<String, CountrySpecificId> {
        override fun convert(source: String): CountrySpecificId {
            return CountrySpecificId(source)
        }
    }

    @TestOnly
    companion object
}
