package net.testiprod.fakeapi

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel

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


    fun getModel(modelName: String?): ChatLanguageModel {
        return when (modelName) {
            "azure" -> azureOpenAiModel
            "openai" -> openAiModel
            else -> throw IllegalArgumentException("Unknown model name: $modelName")
        }
    }

}
