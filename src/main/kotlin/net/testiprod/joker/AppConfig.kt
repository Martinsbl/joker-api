package net.testiprod.joker

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.io.File

object AppConfig {

    val azureConfig: AzureConfig
    val openAiConfig: OpenAiConfig
    val jokeTopics: List<String>
    val chatMemoryMaxSize: Int

    init {
        val config = getConfigFromFile()

        azureConfig = getAzureConfig(config)
        openAiConfig = getOpenAiConfig(config)

        jokeTopics = config.getConfig("jokes").getStringList("topics")
        chatMemoryMaxSize = config.getInt("chatMemoryMaxSize")
    }

    private fun getConfigFromFile(): Config {
        return ConfigFactory
            .parseFile(File("config/app-config.conf"))
            .resolve()
    }

    private fun getAzureConfig(config: Config): AzureConfig {
        val azureConfig = config.getConfig("models.azure")
        return AzureConfig(
            azureConfig.getString("key"),
            azureConfig.getString("endpoint"),
            azureConfig.getString("deployment-name"),
        )
    }

    private fun getOpenAiConfig(config: Config): OpenAiConfig {
        val openAiConfig = config.getConfig("models.openai")
        return OpenAiConfig(
            openAiConfig.getString("key"),
            openAiConfig.getString("model-name"),
        )
    }

    data class AzureConfig(
        val apiKey: String,
        val apiEndpoint: String,
        val deploymentName: String,
    )

    data class OpenAiConfig(
        val apiKey: String,
        val modelName: String,
    )
}
