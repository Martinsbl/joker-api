package net.testiprod.fakeapi

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.models.ApiInfo
import net.testiprod.fakeapi.plugins.configureChatRouting
import net.testiprod.fakeapi.plugins.configureJokeRouting

fun main() {
    embeddedServer(
        CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    configureCors()
    configureSerialization()
    routing {
        route("api"){
            configureChatRouting()
            configureJokeRouting()
            get("/version") {
                call.respond(ApiInfo(BuildConfig.VERSION))
            }
        }
    }
}


fun Application.configureCors() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)

        allowHeader(HttpHeaders.ContentType)

        anyHost() // Probably shouldn't do this
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson()
    }
}
