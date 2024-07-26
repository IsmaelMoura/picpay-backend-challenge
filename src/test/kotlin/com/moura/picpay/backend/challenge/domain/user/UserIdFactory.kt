package com.moura.picpay.backend.challenge.domain.user

import kotlin.random.Random

fun UserId.Companion.random() = UserId(Random.nextLong(1, Long.MAX_VALUE))
