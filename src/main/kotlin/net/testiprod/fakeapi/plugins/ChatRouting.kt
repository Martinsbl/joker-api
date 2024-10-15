package net.testiprod.fakeapi.plugins

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.service.AiServices
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.Utils.getModel
import net.testiprod.fakeapi.models.AiResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.fakeapi.plugins.configureChatRouting")

fun Route.configureChatRouting() {
    get("/chat") {
        val prompt = call.request.queryParameters["prompt"]
        requireNotNull(prompt)
        logger.trace("\"/chat\" called with prompt = '$prompt'")

        val model: ChatLanguageModel = getModel(call.request)

        val myAiAssistant = AiServices.builder(MyAIAssistant::class.java)
            .chatLanguageModel(model)
            .build()

        val aiAnswer = myAiAssistant.chat(prompt)
        call.respond(
            AiResponse(
                title = prompt,
                body = aiAnswer,
            ),
        )
    }
}
