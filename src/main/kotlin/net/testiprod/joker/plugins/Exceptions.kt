package net.testiprod.joker.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

data class ExceptionResponse(
    val status: HttpStatusCode,
    val message: String,
    val exceptionName: String,
    val stackTrace: String
) {
    constructor(throwable: Throwable) : this(
        HttpStatusCode.InternalServerError,
        throwable.message ?: "null",
        throwable::class.java.simpleName,
        throwable.stackTraceToString(),
    )
}


fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is Exception -> call.respond(HttpStatusCode.InternalServerError, ExceptionResponse(throwable))
            }
        }

    }
}
