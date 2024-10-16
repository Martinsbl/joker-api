package net.testiprod.joker.ai

import dev.langchain4j.agent.tool.Tool
import kotlin.math.sqrt
import org.slf4j.LoggerFactory

class Tools {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Tool("Returns a square root of a given number")
    fun squareRoot(x: Double): Double {
        return sqrt(x).also {
            logger.debug("squareRoot() called with: x = $x, returned = $it")
        }
    }
}
