package com.moura.picpay.backend.challenge.utils

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

suspend inline fun <reified T : Any> WebTestClient.ResponseSpec.returnListBody(): List<T> {
    return returnResult<T>().responseBody.asFlow().toList()
}

suspend inline fun <reified T : Any> WebTestClient.ResponseSpec.returnSingleBody(): T {
    return returnListBody<T>().single()
}
