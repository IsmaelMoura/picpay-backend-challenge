package com.moura.picpay.backend.challenge.infrastructure.http.transfer.api

import com.moura.picpay.backend.challenge.domain.user.model.UserId
import com.moura.picpay.backend.challenge.domain.user.model.random
import com.moura.picpay.backend.challenge.infrastructure.http.transfer.api.validation.TransferValidator
import java.math.BigDecimal
import kotlin.random.Random

fun TransferRequest.Companion.create(
    value: BigDecimal = BigDecimal.valueOf(Random.nextDouble(TransferValidator.MIN_AMOUNT_VALUE.toDouble(), Double.MAX_VALUE)),
    payer: UserId = UserId.random(),
    payee: UserId = UserId.random(),
): TransferRequest =
    TransferRequest(
        value = value,
        payer = payer,
        payee = payee,
    )
