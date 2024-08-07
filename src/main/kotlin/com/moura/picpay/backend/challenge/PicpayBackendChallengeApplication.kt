package com.moura.picpay.backend.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.moura.picpay.backend.challenge")
class PicpayBackendChallengeApplication

fun main(args: Array<String>) {
    runApplication<PicpayBackendChallengeApplication>(*args)
    Hooks.enableAutomaticContextPropagation()
}
