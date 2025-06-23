plugins {
    val kotlinVersion = "2.1.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0-rc.1"
    id("com.google.protobuf") version "0.9.5"
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

val kotlinLoggingVersion = "7.0.7"
val ulidjVersion = "2.0.0"
val protobufVersion = "4.31.1"
val kotestVersion = "6.0.0.M4"
val apacheCommonsVersion = "3.17.0"
val springMockkVersion = "4.0.2"

dependencies {
    // Web
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // ULID
    implementation("io.azam.ulidj:ulidj:$ulidjVersion")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // Protobuf
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                create("kotlin")
            }
        }
    }
}

val integrationTest by tasks.register<Test>("integrationTest") {
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
