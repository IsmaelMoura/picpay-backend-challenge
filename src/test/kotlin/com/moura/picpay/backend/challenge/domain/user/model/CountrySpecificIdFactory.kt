package com.moura.picpay.backend.challenge.domain.user.model

import com.moura.picpay.backend.challenge.domain.masking.asSensitiveData
import io.azam.ulidj.ULID
import kotlin.random.Random

fun CountrySpecificId.Companion.random() = CountrySpecificId(ULID.random().asSensitiveData())

fun CountrySpecificId.Companion.randomList(size: Int = Random.nextInt(10, 20)) = List(size) { random() }
