package com.moura.picpay.backend.challenge.domain.user

import io.azam.ulidj.ULID

fun CountrySpecificId.Companion.random() = CountrySpecificId(ULID.random())
