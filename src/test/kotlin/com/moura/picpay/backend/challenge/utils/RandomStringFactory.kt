package com.moura.picpay.backend.challenge.utils

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic

fun String.Companion.randomFullName() = "${randomAlphabetic(10, 20)} ${randomAlphabetic(10, 20)}"

fun String.Companion.randomEmail() = "${randomAlphabetic(10, 20)}-${randomAlphabetic(10, 20)}@email.com"
