package com.moura.picpay.backend.challenge.domain.transfer.persistence

import com.moura.picpay.backend.challenge.domain.transfer.TransferId
import com.moura.picpay.backend.challenge.domain.user.UserId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

@Table(name = "transfers")
data class TransferEntity(
    val id: TransferId = TransferId.random(),
    val payeeId: UserId,
    val payerId: UserId,
    val amount: BigDecimal,

    @CreatedDate
    val createdAt: Instant? = null,
) : Serializable


