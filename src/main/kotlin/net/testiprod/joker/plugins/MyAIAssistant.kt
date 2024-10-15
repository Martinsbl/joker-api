package net.testiprod.joker.plugins

import dev.langchain4j.service.SystemMessage

interface MyAIAssistant {
    @SystemMessage("You are The Joker from the Batman movies")
    fun chat(message: String): String
}
