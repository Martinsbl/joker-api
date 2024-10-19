package net.testiprod.joker

import com.azure.ai.openai.models.ChatCompletionsJsonResponseFormat
import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import io.ktor.server.request.*
import net.testiprod.joker.ai.MemoryProvider
import net.testiprod.joker.ai.Tools
import net.testiprod.joker.plugins.MyAIAssistant

object AiAssistantFactory {

    private val memoryProvider = MemoryProvider()

    private fun getModel(
        modelProvider: String?,
        modelName: String?,
        getJsonResponse: Boolean,
    ): ChatLanguageModel {
        return when (modelProvider) {
            "azure" -> {
                val builder = AzureOpenAiChatModel.builder()
                    .apiKey(AppConfig.azureConfig.apiKey)
                    .deploymentName(modelName)
                    .endpoint(AppConfig.azureConfig.apiEndpoint)
                    .logRequestsAndResponses(true)
                if (getJsonResponse) {
                    builder.responseFormat(ChatCompletionsJsonResponseFormat())
                }
                builder.build()
            }

            "openai" -> {
                val builder = OpenAiChatModel.builder()
                    .apiKey(AppConfig.openAiConfig.apiKey)
                    .modelName(modelName)
                    .logRequests(true)
                    .logResponses(true)
                if (getJsonResponse) {
                    builder.responseFormat("json_schema").strictJsonSchema(true)
                }

                builder.build()
            }

            "ollama" -> {
                val builder = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(modelName)
                    .logRequests(true)
                    .logResponses(true)
                if (getJsonResponse) {
                    builder.format("json")
                }

                builder.build()
            }

            else -> throw IllegalArgumentException("Unknown model name: $modelName")
        }
    }

    fun getAssistant(
        request: ApplicationRequest,
        useChatMemory: Boolean,
        getJsonResponse: Boolean = false,
        tools: Tools? = null,
    ): Pair<MyAIAssistant, ChatLanguageModel> {
        val userId = request.queryParameters["userId"]
        requireNotNull(userId) { "Query param 'userId' must not be null" }
        val modelProvider = request.queryParameters["modelProvider"]
        requireNotNull(modelProvider) { "Query param 'modelProvider' must not be null" }
        val modelName = request.queryParameters["modelName"] ?: "gpt-4o-mini"

        val model = getModel(modelProvider, modelName, getJsonResponse)

        val assistantBuilder = AiServices.builder(MyAIAssistant::class.java)
            .chatLanguageModel(model)

        tools?.let {
            assistantBuilder.tools(Tools())
        }

        if (useChatMemory) {
            assistantBuilder.chatMemoryProvider { memoryProvider.get(userId) }
        }

        val assistant = assistantBuilder.build()

        return Pair(assistant, model)
    }

}
