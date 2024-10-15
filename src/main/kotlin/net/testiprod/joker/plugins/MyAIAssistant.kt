package net.testiprod.joker.plugins

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage

interface MyAIAssistant {

    fun chat(message: String): Response<AiMessage>

    @SystemMessage("You are The Joker from the Batman movies")
    fun chatWithTheJoker(message: String): Response<AiMessage>
}
