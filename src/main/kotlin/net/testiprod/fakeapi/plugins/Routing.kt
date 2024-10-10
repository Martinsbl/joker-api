package net.testiprod.fakeapi.plugins

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.fakeapi.AppConfig
import net.testiprod.fakeapi.BuildConfig
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

data class AiResponse(
    val title: String,
    val body: String,
)

data class ApiInfo(
    val version: String,
)

fun Application.configureApiRouting() {
    routing {
        route("/api") {
            get("/version") {
                call.respond(ApiInfo(BuildConfig.VERSION))
            }
            get("/chat") {
                val prompt = call.request.queryParameters["prompt"]
                requireNotNull(prompt)
                val model = call.request.queryParameters["model"]
                logger.trace("\"/chat\" called with model = '$model', prompt = '$prompt'")

                val aiAnswer = askAi(getModel(model), prompt)
                call.respond(
                    AiResponse(
                        title = prompt,
                        body = aiAnswer
                    )
                )
            }
            get("/joke") {
                val model = call.request.queryParameters["model"]
                logger.trace("\"/joke\" called with model = '$model'")

                val topic = AppConfig.jokeTopics.random()
                val aiAnswer = askAi(getModel(model), "Tell me a joke about $topic")
                call.respond(
                    AiResponse(
                        title = "Here is a joke about $topic",
                        body = aiAnswer
                    )
                )
            }
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

private fun askAi(chatModel: ChatLanguageModel, prompt: String): String {
    val answer = chatModel.generate(prompt)?.replace("\n\n", "\n")
    checkNotNull(answer)
    logger.trace("Prompt: $prompt")
    logger.trace("Answer: $answer")
    return answer
}
