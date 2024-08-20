plugins {
    alias(libs.plugins.spring.boot) apply true
    alias(libs.plugins.spring.dependency.management) apply true
    alias(libs.plugins.kotlin.jvm) apply true
    alias(libs.plugins.kotlin.spring) apply true
    alias(libs.plugins.ktlint) apply true
}

group = "com.moura"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Web
    implementation(libs.spring.webflux)
    implementation(libs.jackson.kotlin)

    // Database
    implementation(libs.spring.r2dbc)
    runtimeOnly(libs.flyway.postgres)
    runtimeOnly(libs.postgres.driver.r2dbc)
    runtimeOnly(libs.postgres.driver.jdbc)

    // Observability / Metrics
    implementation(libs.spring.actuator)
    implementation(libs.micrometer.tracing.brave)
    implementation(libs.micrometer.prometheus)
    implementation(libs.kotlin.logging.jvm)

    // Kotlin
    implementation(libs.reactor.kotlin.extensions)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.coroutines.reactor)
    implementation(libs.kotlin.coroutines.slf4j)

    // ULID
    implementation(libs.ulidj)

    testImplementation(libs.spring.test)
    testImplementation(libs.spring.testcontainers)
    testImplementation(libs.reactor.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotest.junit)
    testImplementation(libs.apache.commons)
    testImplementation(libs.spring.mockk)
    testImplementation(libs.junit.plataform)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

val integrationTest =
    tasks.register<Test>("integrationTest") {
        group = "verification"
        description = "Execute all integration tests"

        useJUnitPlatform {
            includeTags.add(name)
        }
    }

tasks {
    test {
        useJUnitPlatform {
            excludeTags.add(integrationTest.name)
        }
    }

    addKtlintFormatGitPreCommitHook {
        installHook()
    }
}
