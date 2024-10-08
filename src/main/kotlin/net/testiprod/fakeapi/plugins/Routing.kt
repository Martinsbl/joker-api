package net.testiprod.fakeapi.plugins

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiChatModelName
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val apiKey: String = System.getenv("AZURE_OPENAI_KEY")
private val apiEndpoint: String = System.getenv("AZURE_ENDPOINT_URL")
private val azureOpenAiModel: AzureOpenAiChatModel = AzureOpenAiChatModel.builder()
    .apiKey(apiKey)
    .deploymentName("gpt-4")
    .endpoint(apiEndpoint)
    .build()


private val openAiModel: OpenAiChatModel = OpenAiChatModel.builder()
    .apiKey("demo")
    .modelName(OpenAiChatModelName.GPT_4_O_MINI)
    .build();

fun Application.configureRouting() {
    routing {
        get("/chat") {
            val promptQuery = call.request.queryParameters["promptQuery"]
            val model = call.request.queryParameters["model"]

            val aiChat = askAi(getModel(model), promptQuery ?: "Tell me a joke")
            call.respondText(aiChat ?: "No response from AI")
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

private fun askAi(chatModel: ChatLanguageModel, promptQuery: String): String? {
    val answer = chatModel.generate(promptQuery)
    println(promptQuery)
    println(answer)
    return answer
}
