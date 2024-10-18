package net.testiprod.joker.plugins

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.AiAssistantFactory
import net.testiprod.joker.models.AiExtendedResponse
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureAiTestRouting")

fun Route.configureAiDialogRouting() {
    get("/dialog") {
        val prompt = call.request.queryParameters["prompt"]
        requireNotNull(prompt)
        logger.trace("\"/test\" called with prompt = '$prompt'")

        val (dialogue, model) = runDialog(call, prompt)

        call.respond(
            dialogue.map {
                AiExtendedResponse(
                    model,
                    prompt,
                    it,
                )
            },
        )
    }
}

private fun runDialog(call: ApplicationCall, prompt: String): Pair<List<Response<AiMessage>>, ChatLanguageModel> {
    val (assistant, model) = AiAssistantFactory.getAssistant(call.request, true)
    val answers = mutableListOf<Response<AiMessage>>()

    var jokerAnswer = assistant.chatWithTheJoker(prompt)
    answers.add(jokerAnswer)
    logger.trace("The Joker: ${jokerAnswer.getPrettyAnswer()}")

    repeat(4) {
        val batmanAnswer = assistant.chatWithTheBatman(jokerAnswer.getPrettyAnswer())
        answers.add(batmanAnswer)
        logger.trace("Batman: ${batmanAnswer.getPrettyAnswer()}")
        jokerAnswer = assistant.chatWithTheJoker(batmanAnswer.getPrettyAnswer())
        answers.add(jokerAnswer)
        logger.trace("The Joker: ${jokerAnswer.getPrettyAnswer()}")
    }
    return Pair(answers, model)

}

private fun Response<AiMessage>.getPrettyAnswer(): String {
    return content().text().replace("\n\n", " ")
}
