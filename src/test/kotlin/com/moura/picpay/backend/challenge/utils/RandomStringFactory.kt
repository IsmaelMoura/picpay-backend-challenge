package com.moura.picpay.backend.challenge.utils

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import kotlin.random.Random

fun String.Companion.randomFullName() = "${randomAlphabetic(10, 20)} ${randomAlphabetic(10, 20)}"

fun String.Companion.randomEmail() = "${randomAlphabetic(10, 20)}-${randomAlphabetic(10, 20)}@email.com"

fun String.Companion.randomFullNameList(size: Int = Random.nextInt(10, 20)) = List(size) { randomFullName() }

fun String.Companion.randomEmailList(size: Int = Random.nextInt(10, 20)) = List(size) { randomEmail() }
