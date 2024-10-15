package net.testiprod.joker.ai

import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import net.testiprod.joker.AppConfig
import org.slf4j.LoggerFactory

class MemoryProvider : ChatMemoryProvider {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val memoryMap = HashMap<Int, ChatMemory>()

    override fun get(id: Any?): ChatMemory {
        val idHash = id.hashCode()
        logger.trace("Getting chat memory for id: $idHash")
        memoryMap[idHash]?.let {
            return it
        }

        val chatMemory: ChatMemory = MessageWindowChatMemory.builder()
            .maxMessages(AppConfig.chatMemoryMaxSize)
            .id(id)
            .build()

        memoryMap[idHash] = chatMemory
        return chatMemory
    }
}
