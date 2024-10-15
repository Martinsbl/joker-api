package net.testiprod.fakeapi.plugins

import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.AppConfig
import net.testiprod.fakeapi.Utils.getModel
import net.testiprod.fakeapi.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.fakeapi.plugins.configureJokeRouting")

fun Route.configureJokeRouting() {
    get("/joke") {
        logger.trace("\"/joke\" called")
        val model: ChatLanguageModel = getModel(call.request)
        val aiResponse = generateJoke(model)
        call.respond(aiResponse)
    }
}

private fun generateJoke(chatModel: ChatLanguageModel): AiExtendedResponse {
    val topic = AppConfig.jokeTopics.random()
    val prompt = "Tell me a joke about $topic"
    val chatMessage = UserMessage(prompt)
    val answer = chatModel.generate(chatMessage)
    return AiExtendedResponse(
        chatModel,
        "Here is a joke about $topic",
        answer,
    )
}
