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

    @Tool(
        """
        This function controls your left and right wheels. 
        The left and right values should be withing the range -1 to 1.
        You move forwards by calling this function with positive left and right values.
        You move backwards by calling this function with negative left and right values.
        You steer left by calling this function with right > left.
        You steer right by calling this function with left > right.
    """,
    )
    fun move(left: Double, right: Double) {
        logger.debug("drive() called with: left = $left, right = $right")
    }
}
