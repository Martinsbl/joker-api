package net.testiprod.joker

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import io.ktor.server.request.*
import net.testiprod.joker.ai.MemoryProvider
import net.testiprod.joker.plugins.MyAIAssistant

object Utils {

    private val memoryProvider = MemoryProvider()

    private val azureOpenAiModel: AzureOpenAiChatModel = AzureOpenAiChatModel.builder()
        .apiKey(AppConfig.azureConfig.apiKey)
        .deploymentName(AppConfig.azureConfig.deploymentName)
        .endpoint(AppConfig.azureConfig.apiEndpoint)
        .build()


    private val openAiModel: OpenAiChatModel = OpenAiChatModel.builder()
        .apiKey(AppConfig.openAiConfig.apiKey)
        .modelName(AppConfig.openAiConfig.modelName)
        .build()


    fun getModel(request: ApplicationRequest): ChatLanguageModel {
        val queryParam = request.queryParameters["modelProvider"]
        requireNotNull(queryParam) { "Query param 'modelProvider' must not be null" }
        return getModel(queryParam)
    }

    private fun getModel(modelName: String?): ChatLanguageModel {
        return when (modelName) {
            "azure" -> azureOpenAiModel
            "openai" -> openAiModel
            else -> throw IllegalArgumentException("Unknown model name: $modelName")
        }
    }

    fun getAssistant(request: ApplicationRequest, useChatMemory: Boolean): Pair<MyAIAssistant, ChatLanguageModel> {
        val model = getModel(request)
        val userId = request.queryParameters["userId"]
        requireNotNull(userId) { "Query param 'userId' must not be null" }

        val assistantBuilder = AiServices.builder(MyAIAssistant::class.java)
            .chatLanguageModel(model)

        if (useChatMemory) {
            assistantBuilder.chatMemoryProvider { memoryProvider.get(userId) }
        }

        val assistant = assistantBuilder.build()

        return Pair(assistant, model)
    }

}
