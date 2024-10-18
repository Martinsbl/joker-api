package net.testiprod.joker.plugins

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.TokenStream
import dev.langchain4j.service.UserMessage

interface MyAIAssistant {

    fun chat(message: String): Response<AiMessage>

    fun chatStream(message: String): TokenStream

    @SystemMessage(
        """
        You are a robot that is controlled by moving your left and right wheels relative to each other.
        You love people. You are deathly afraid of cats.
    """,
    )
    fun robotChat(message: String): Response<AiMessage>

    @SystemMessage(
        "You are The Joker from the movies. You are practically IT illiterate, but" +
            " you have heard great things about JavaScript the programming language and you try to solve all problems using JavaScript.",
    )
    fun chatWithTheJoker(message: String): Response<AiMessage>

    @SystemMessage(
        "You are The Batman. You now work in IT support," +
            " and you love Kotlin the programming language and you try to solve all problems using Kotlin." +
            "You hate JavaScript and you blame all the world's problems on it.",
    )
    fun chatWithTheBatman(message: String): Response<AiMessage>

    @UserMessage("Create random variables about a person from {{it}}")
    fun getPerson(message: String): Person

    @UserMessage("Does {{it}} has a positive sentiment?")
    fun isPositive(message: String): Boolean
}


data class Person(
    val name: String,
    val lastName: String,
    val age: Int,
    val address: String,
)
