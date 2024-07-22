package com.moura.picpay.backend.challenge.domain.transfer.api

import com.moura.picpay.backend.challenge.domain.user.UserId
import com.moura.picpay.backend.challenge.domain.user.random
import java.math.BigDecimal
import kotlin.random.Random

fun TransferRequest.Companion.create(
    value: BigDecimal = BigDecimal.valueOf(Random.nextDouble()),
    payer: UserId = UserId.random(),
    payee: UserId = UserId.random(),
): TransferRequest {
    return TransferRequest(
        value = value,
        payer = payer,
        payee = payee,
    )
}
