package com.moura.picpay.backend.challenge.domain.user.model

import com.moura.picpay.backend.challenge.domain.masking.SensitiveData
import org.jetbrains.annotations.TestOnly
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.io.Serializable

@JvmInline
value class CountrySpecificId(val value: SensitiveData<String>) : Serializable {
    @Component
    object ToStringConverter : Converter<CountrySpecificId, SensitiveData<String>> {
        override fun convert(source: CountrySpecificId): SensitiveData<String> {
            return source.value
        }
    }

    @Component
    object FromStringConverter : Converter<SensitiveData<String>, CountrySpecificId> {
        override fun convert(source: SensitiveData<String>): CountrySpecificId {
            return CountrySpecificId(source)
        }
    }

    @TestOnly
    companion object
}
