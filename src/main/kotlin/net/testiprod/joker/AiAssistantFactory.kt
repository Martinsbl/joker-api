package net.testiprod.joker

import com.azure.ai.openai.models.ChatCompletionsJsonResponseFormat
import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import io.ktor.server.request.*
import net.testiprod.joker.ai.MemoryProvider
import net.testiprod.joker.ai.Tools
import net.testiprod.joker.plugins.MyAIAssistant

object AiAssistantFactory {

    private val memoryProvider = MemoryProvider()

    private fun getModel(
        modelName: String?,
        getJsonResponse: Boolean,
    ): ChatLanguageModel {
        return when (modelName) {
            "azure" -> {
                val builder = AzureOpenAiChatModel.builder()
                    .apiKey(AppConfig.azureConfig.apiKey)
                    .deploymentName(AppConfig.azureConfig.deploymentName)
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
                    .modelName(AppConfig.openAiConfig.modelName)
                    .logRequests(true)
                    .logResponses(true)
                if (getJsonResponse) {
                    builder.responseFormat("json_schema").strictJsonSchema(true)
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
    ): Pair<MyAIAssistant, ChatLanguageModel> {
        val modelProvider = request.queryParameters["modelProvider"]
        requireNotNull(modelProvider) { "Query param 'modelProvider' must not be null" }

        val model = getModel(modelProvider, getJsonResponse)
        val userId = request.queryParameters["userId"]
        requireNotNull(userId) { "Query param 'userId' must not be null" }

        val assistantBuilder = AiServices.builder(MyAIAssistant::class.java)
            .chatLanguageModel(model)
            .tools(Tools())

        if (useChatMemory) {
            assistantBuilder.chatMemoryProvider { memoryProvider.get(userId) }
        }

        val assistant = assistantBuilder.build()

        return Pair(assistant, model)
    }

}
