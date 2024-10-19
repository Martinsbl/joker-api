package net.testiprod.joker.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.testiprod.joker.BuildConfig
import net.testiprod.joker.models.ApiInfo
import net.testiprod.joker.models.ModelConfig
import net.testiprod.joker.models.ModelProvider
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("net.testiprod.joker.plugins.configureInfoRouting")

fun Route.configureInfoRouting() {
    get("/info") {
        logger.trace("\"/info\" called")
        val apiInfo = ApiInfo(
            BuildConfig.VERSION,
            listOf(
                ModelConfig(ModelProvider("openai", "OpenAI"), listOf("gpt-4o-mini")),
                ModelConfig(ModelProvider("azure", "Azure OpenAI"), listOf("gpt-4o-mini")),
            ),
        )
        call.respond(apiInfo)
    }
}
