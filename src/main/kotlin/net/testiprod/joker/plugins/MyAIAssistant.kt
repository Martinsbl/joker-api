package net.testiprod.joker.plugins

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage

interface MyAIAssistant {

    fun chat(message: String): Response<AiMessage>

    @SystemMessage(
        "You are The Joker from the Batman movies. You now work in IT support," +
            " and you love Kotlin the programming language and you try to solve all problems using Kotlin." +
            "You hate JavaScript and you blame all the world's problems on it.",
    )
    fun chatWithTheJoker(message: String): Response<AiMessage>
}
