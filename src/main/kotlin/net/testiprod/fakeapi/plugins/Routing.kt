package net.testiprod.fakeapi.plugins

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.AppConfig
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.fakeapi.plugins.Routing.chat")

private val azureOpenAiModel: AzureOpenAiChatModel = AzureOpenAiChatModel.builder()
    .apiKey(AppConfig.azureConfig.apiKey)
    .deploymentName(AppConfig.azureConfig.deploymentName)
    .endpoint(AppConfig.azureConfig.apiEndpoint)
    .build()


private val openAiModel: OpenAiChatModel = OpenAiChatModel.builder()
    .apiKey(AppConfig.openAiConfig.apiKey)
    .modelName(AppConfig.openAiConfig.modelName)
    .build()

fun Application.configureRouting() {
    routing {
        get("/chat") {
            val prompt = call.request.queryParameters["prompt"]
            val model = call.request.queryParameters["model"]
            logger.trace("/chat called with model = '$model', prompt = '$prompt'")

            val topic = AppConfig.jokeTopics.random()
            val promptResponse = askAi(getModel(model), prompt ?: "Tell me a joke about $topic")
            val response = promptResponse?.let {
                "Here is a joke about $topic:\n$it"
            } ?: "No response from AI"
            call.respondText(response)
        }
    }
}

fun getModel(modelName: String?): ChatLanguageModel {
    return when (modelName) {
        "azure" -> azureOpenAiModel
        "openai" -> openAiModel
        else -> throw IllegalArgumentException("Unknown model name: $modelName")
    }
}

private fun askAi(chatModel: ChatLanguageModel, prompt: String): String? {
    val answer = chatModel.generate(prompt)?.replace("\n\n", "\n")
    logger.trace("Prompt: $prompt")
    logger.trace("Answer: $answer")
    return answer
}
