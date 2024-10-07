import java.net.URI

val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("com.google.cloud.tools.jib") version "3.4.1"
}

group = "net.testiprod"
version = "0.0.1"

application {
    mainClass.set("net.testiprod.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

jib {
    from {
        image = "openjdk:17-jdk-alpine"
    }
    to {
        image = "${findProperty("docker-registry.haven.url") as String? ?: "missing registry url"}/fakeapi"
        auth {
            username = findProperty("docker-registry.haven.username") as String? ?: "missing registry username"
            password = findProperty("docker-registry.haven.password") as String? ?: "missing registry password"
        }
    }
    container {
        mainClass = "net.testiprod.fakeapi.ApplicationKt"
        ports = listOf("80")
        volumes = listOf(
            "/log",
            "/files",
            "/config"
        )
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
