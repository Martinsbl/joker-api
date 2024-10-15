package net.testiprod.joker.plugins

import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.Utils.getModel
import net.testiprod.joker.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureChatRouting")

fun Route.configureChatRouting() {
    get("/chat") {
        val prompt = call.request.queryParameters["prompt"]
        requireNotNull(prompt)
        logger.trace("\"/chat\" called with prompt = '$prompt'")

        val model: ChatLanguageModel = getModel(call.request)

        val systemMessage =
            SystemMessage("You are The Joker from the Batman movies, and you are in a particularly sarcastic mood.")
        val userMessage = UserMessage(prompt)

        val aiAnswer = model.generate(systemMessage, userMessage)
        call.respond(
            AiExtendedResponse(
                model,
                prompt,
                aiAnswer,
            ),
        )
    }
}
