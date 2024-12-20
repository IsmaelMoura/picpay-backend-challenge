package com.moura.picpay.backend.challenge.configuration

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        return jsonMapper {
            addModules(
                JavaTimeModule(),
                kotlinModule {
                    enable(KotlinFeature.NullIsSameAsDefault)
                    enable(KotlinFeature.SingletonSupport)
                },
            )
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        }
    }
}
