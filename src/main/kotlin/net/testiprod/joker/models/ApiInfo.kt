package net.testiprod.joker.models

data class ApiInfo(
    val version: String,
    val modelConfigs: List<ModelConfig>,
)

data class ModelConfig(
    val modelProvider: ModelProvider,
    val supportedModels: List<String>,
)

data class ModelProvider(
    val id: String,
    val friendlyName: String,
)
