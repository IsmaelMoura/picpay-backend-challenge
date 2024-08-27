package com.moura.picpay.backend.challenge.domain.user.model

import kotlin.random.Random

fun UserId.Companion.random() = UserId(Random.nextLong(1, Long.MAX_VALUE))
