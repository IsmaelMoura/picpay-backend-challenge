package com.moura.picpay.backend.challenge.configuration

import com.moura.picpay.backend.challenge.domain.transfer.model.TransferId
import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserId
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver

@Configuration
@EnableR2dbcAuditing
class R2dbcConfiguration {
    @Bean
    fun r2dbcCustomConversions(connectionFactory: ConnectionFactory): R2dbcCustomConversions {
        return R2dbcCustomConversions.of(
            DialectResolver.getDialect(connectionFactory),
            setOf(
                UserId.ToLongConverter,
                UserId.FromLongConverter,
                TransferId.ToStringConverter,
                TransferId.FromStringConverter,
                CountrySpecificId.ToStringConverter,
                CountrySpecificId.FromStringConverter,
            ),
        )
    }
}
