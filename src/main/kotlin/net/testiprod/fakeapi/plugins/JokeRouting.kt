package net.testiprod.fakeapi.plugins

import dev.langchain4j.model.chat.ChatLanguageModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.AppConfig
import net.testiprod.fakeapi.Utils.getModel
import net.testiprod.fakeapi.models.AiResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.fakeapi.plugins.configureJokeRouting")

fun Route.configureJokeRouting() {
    get("/joke") {
        val model = call.request.queryParameters["model"]
        logger.trace("\"/joke\" called with model = '$model'")

        val topic = AppConfig.jokeTopics.random()
        val aiAnswer = askAi(getModel(model), "Tell me a joke about $topic")
        call.respond(
            AiResponse(
                title = "Here is a joke about $topic",
                body = aiAnswer,
            ),
        )
    }
}

private fun askAi(chatModel: ChatLanguageModel, prompt: String): String {
    val answer = chatModel.generate(prompt)?.replace("\n\n", "\n")
    checkNotNull(answer)
    logger.trace("Prompt: $prompt")
    logger.trace("Answer: $answer")
    return answer
}
