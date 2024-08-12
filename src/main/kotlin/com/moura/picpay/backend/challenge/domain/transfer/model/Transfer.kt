package com.moura.picpay.backend.challenge.domain.transfer.model

import com.moura.picpay.backend.challenge.domain.user.User
import java.math.BigDecimal

data class Transfer(
    val id: TransferId,
    val payee: User,
    val payer: User,
    val amount: BigDecimal,
)
