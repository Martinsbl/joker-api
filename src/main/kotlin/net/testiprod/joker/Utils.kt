package net.testiprod.joker

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.ktor.server.request.*

object Utils {

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

    fun getModel(modelName: String?): ChatLanguageModel {
        return when (modelName) {
            "azure" -> azureOpenAiModel
            "openai" -> openAiModel
            else -> throw IllegalArgumentException("Unknown model name: $modelName")
        }
    }

}
