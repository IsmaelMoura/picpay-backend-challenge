package com.moura.picpay.backend.challenge.domain.user.persistence

import com.moura.picpay.backend.challenge.domain.user.CountrySpecificId
import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.UserType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

@Table(name = "users")
data class UserEntity(
    @Id
    val id: UserId? = null,
    val countrySpecificId: CountrySpecificId,
    val fullName: String,
    val email: String,
    val password: String,
    val type: UserType,
    val balance: BigDecimal,
    @CreatedDate
    val createdAt: Instant? = null,
    @LastModifiedDate
    val modifiedAt: Instant? = null,
) : Serializable
