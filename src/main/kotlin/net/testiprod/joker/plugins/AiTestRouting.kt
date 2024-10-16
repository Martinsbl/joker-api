package net.testiprod.joker.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.Utils
import net.testiprod.joker.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureAiTestRouting")

fun Route.configureAiTestRouting() {
    route("/test") {
        get("/test") {
            call.log()
            val prompt = call.request.queryParameters["prompt"]
            requireNotNull(prompt)

            val (assistant, model) = Utils.getAssistant(call.request, false, true)
            val aiAnswer = assistant.chat(prompt)

            call.respond(
                AiExtendedResponse(
                    model,
                    prompt,
                    aiAnswer,
                ),
            )
        }
        get("/person") {
            call.log()
            val prompt = call.request.queryParameters["prompt"]
            requireNotNull(prompt)

            val (assistant, model) = Utils.getAssistant(call.request, false, true)
            val person = assistant.getPerson(prompt)

            call.respond(
                person,
            )
        }
        get("/is-positive") {
            call.log()
            val prompt = call.request.queryParameters["prompt"]
            requireNotNull(prompt)

            val (assistant, model) = Utils.getAssistant(call.request, false, true)
            val person = assistant.isPositive(prompt)

            call.respond(
                person,
            )
        }
        get("/stream") {
            call.log()
            val prompt = call.request.queryParameters["prompt"]
            requireNotNull(prompt)

            val (assistant, model) = Utils.getAssistant(call.request, true)
            val stream = assistant.chatStream(prompt)
            stream.onNext(System.out::println)
                .onComplete(System.out::println)
                .onError(Throwable::printStackTrace)
                .start();
            call.respondText("adsølfkj ")
        }
    }
}


private fun ApplicationCall.log() {
    val params = request.queryParameters
    logger.trace(
        "\"${request.path()}\" called with params = [${
            params.entries().joinToString { "${it.key}=${it.value}" }
        }"
    )
}
