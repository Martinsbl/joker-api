package net.testiprod.fakeapi

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import net.testiprod.fakeapi.plugins.configureRouting

fun main() {
    embeddedServer(
        CIO,
        port = 80,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}
