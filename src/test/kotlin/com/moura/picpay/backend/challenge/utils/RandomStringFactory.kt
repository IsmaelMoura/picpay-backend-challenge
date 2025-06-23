package com.moura.picpay.backend.challenge.utils

import kotlin.random.Random
import org.apache.commons.lang3.RandomStringUtils.insecure as insecureRandom

fun String.Companion.randomFullName() = "${insecureRandom().nextAlphabetic(10, 20)} ${insecureRandom().nextAlphabetic(10, 20)}"

fun String.Companion.randomEmail() = "${insecureRandom().nextAlphabetic(10, 20)}-${insecureRandom().nextAlphabetic(10, 20)}@email.com"

fun String.Companion.randomPassword(): String = insecureRandom().nextAlphanumeric(20)

fun String.Companion.randomFullNameList(size: Int = Random.nextInt(10, 20)) = List(size) { randomFullName() }

fun String.Companion.randomEmailList(size: Int = Random.nextInt(10, 20)) = List(size) { randomEmail() }
