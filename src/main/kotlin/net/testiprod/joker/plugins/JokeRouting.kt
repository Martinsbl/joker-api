package net.testiprod.joker.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.AppConfig
import net.testiprod.joker.AiAssistantFactory
import net.testiprod.joker.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureJokeRouting")

fun Route.configureJokeRouting() {
    get("/joke") {
        logger.trace("\"/joke\" called")

        val aiResponse = generateJoke(call.request)
        call.respond(aiResponse)
    }
}

private fun generateJoke(request: ApplicationRequest): AiExtendedResponse {

    val (assistant, model) = AiAssistantFactory.getAssistant(request, false)
    val topic = AppConfig.jokeTopics.random()
    val prompt = "Tell me a joke about $topic"
    val aiAnswer = assistant.chat(prompt)
    return AiExtendedResponse(
        model,
        "Here is a joke about $topic",
        aiAnswer,
    )
}
