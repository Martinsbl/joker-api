package net.testiprod.joker.ai

import dev.langchain4j.agent.tool.Tool
import kotlin.math.sqrt

class Tools {

    @Tool("Returns a square root of a given number")
    fun squareRoot(x: Double): Double {
        return sqrt(x)
    }
}
