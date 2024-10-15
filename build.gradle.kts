val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("com.google.cloud.tools.jib") version "3.4.1"
}

group = "net.testiprod"
version = "0.0.17"

application {
    mainClass.set("net.testiprod.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("dev.langchain4j:langchain4j-azure-open-ai:0.35.0")
    implementation("dev.langchain4j:langchain4j-open-ai:0.35.0")
    implementation("dev.langchain4j:langchain4j:0.35.0")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

jib {
    from {
        image = "openjdk:17-jdk-alpine"
    }
    to {
        image = "${findProperty("docker-registry.haven.url") as String? ?: "missing registry url"}/joker-api"
        tags = setOf("latest", version as String)
        auth {
            username = findProperty("docker-registry.haven.username") as String? ?: "missing registry username"
            password = findProperty("docker-registry.haven.password") as String? ?: "missing registry password"
        }
    }
    container {
        mainClass = "net.testiprod.fakeapi.ApplicationKt"
        ports = listOf("8080")
        volumes = listOf(
            "/log",
            "/files",
            "/config"
        )
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}

tasks.register("generateVersionFile") {
    doLast {
        val versionFile = file("src/main/kotlin/net/testiprod/fakeapi/BuildConfig.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText("""
            package net.testiprod.fakeapi

            object BuildConfig {
                const val VERSION = "${project.version}"
            }
        """.trimIndent())
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("generateVersionFile")
}
