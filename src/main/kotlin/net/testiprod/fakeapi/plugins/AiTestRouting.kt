package net.testiprod.fakeapi.plugins

import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.Utils.getModel
import net.testiprod.fakeapi.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.fakeapi.plugins.configureAiTestRouting")

fun Route.configureAiTestRouting() {
    get("/test") {
        val prompt = call.request.queryParameters["prompt"]
        requireNotNull(prompt)
        logger.trace("\"/test\" called with prompt = '$prompt'")

        val model: ChatLanguageModel = getModel(call.request)

        val chatMessage = UserMessage(prompt)

        val aiResponse = model.generate(chatMessage)

        call.respond(
            AiExtendedResponse(
                model,
                prompt,
                aiResponse,
            ),
        )
    }
}
