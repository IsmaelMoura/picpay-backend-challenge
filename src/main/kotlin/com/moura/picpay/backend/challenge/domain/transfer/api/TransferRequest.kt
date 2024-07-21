package com.moura.picpay.backend.challenge.domain.transfer.api

import com.moura.picpay.backend.challenge.domain.user.UserId
import java.io.Serializable
import java.math.BigDecimal

data class TransferRequest(
    val value: BigDecimal,
    val payer: UserId,
    val payee: UserId,
) : Serializable
