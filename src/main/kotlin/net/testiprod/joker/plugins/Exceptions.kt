package net.testiprod.joker.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

data class ExceptionResponse(
    val status: HttpStatusCode,
    val message: String,
    val exceptionName: String,
    val url: String,
    val stackTrace: String
) {
    constructor(request: ApplicationRequest, throwable: Throwable) : this(
        HttpStatusCode.InternalServerError,
        throwable.message ?: "null",
        throwable::class.java.simpleName,
        "${request.httpMethod.value} ${request.uri}",
        throwable.stackTraceToString(),
    )
}

private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.exceptions")

fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            logger.error("Caught exception in ${call.request.httpMethod.value} ${call.request.uri}", throwable)
            when (throwable) {
                is Exception -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ExceptionResponse(call.request, throwable),
                )
            }
        }

    }
}
