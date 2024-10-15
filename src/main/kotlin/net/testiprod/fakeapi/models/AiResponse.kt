package net.testiprod.fakeapi.models

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response

data class AiExtendedResponse(
    val chatLanguageModel: String,
    val prompt: String,
    val aiResponse: Response<AiMessage>,
    val timeStamp: Long = System.currentTimeMillis(),
) {
    constructor(
        model: ChatLanguageModel,
        prompt: String,
        aiResponse: Response<AiMessage>,
    ) : this(model::class.java.simpleName ?: "null", prompt, aiResponse)
}
