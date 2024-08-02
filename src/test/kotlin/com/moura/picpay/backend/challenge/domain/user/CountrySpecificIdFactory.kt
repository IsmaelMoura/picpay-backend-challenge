package com.moura.picpay.backend.challenge.domain.user

import io.azam.ulidj.ULID
import kotlin.random.Random

fun CountrySpecificId.Companion.random() = CountrySpecificId(ULID.random())

fun CountrySpecificId.Companion.randomList(size: Int = Random.nextInt(10, 20)) = List(size) { random() }
