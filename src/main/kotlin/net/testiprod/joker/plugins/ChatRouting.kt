package net.testiprod.joker.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.AiAssistantFactory
import net.testiprod.joker.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureChatRouting")

fun Route.configureChatRouting() {
    get("/chat") {
        val prompt = call.request.queryParameters["prompt"]
        requireNotNull(prompt)
        logger.trace("\"/chat\" called with prompt = '$prompt'")

        val (assistant, model) = AiAssistantFactory.getAssistant(call.request, true)
        val aiAnswer = assistant.chatWithTheJoker(prompt)
        call.respond(
            AiExtendedResponse(
                model,
                prompt,
                aiAnswer,
            ),
        )
    }
}
