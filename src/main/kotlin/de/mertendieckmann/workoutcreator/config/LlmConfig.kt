package de.mertendieckmann.workoutcreator.config

import dev.langchain4j.model.chat.ChatModel
import dev.langchain4j.model.chat.listener.ChatModelListener
import dev.langchain4j.model.chat.listener.ChatModelResponseContext
import dev.langchain4j.model.openai.OpenAiChatModel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LlmConfig {

    @Value("\${openai.api-key}")
    lateinit var apiKey: String

    @Value("\${openai.model-name:gpt-4o-2024-08-06}")
    lateinit var modelName: String

    @Value("\${openai.api.url:https://api.openai.com/v1}")
    lateinit var apiUrl: String

    @Bean
    fun openAiChatModel(): ChatModel {
        return OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(modelName)
            .baseUrl(apiUrl)
            .listeners(listOf(tokenUsageListener))
            .build()
    }

    @Bean(name = ["jsonLlm"])
    fun openAiJsonChatModel(): ChatModel {
        return OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(modelName)
            .baseUrl(apiUrl)
            .responseFormat("json_object")
            .listeners(listOf(tokenUsageListener))
            .build()
    }

    companion object {

        private val log = KotlinLogging.logger { }

        val tokenUsageListener = object : ChatModelListener {
            override fun onResponse(ctx: ChatModelResponseContext) {
                val u = ctx.chatResponse().tokenUsage()
                log.info { "tokens prompt=${u.inputTokenCount()}, completion=${u.outputTokenCount()}, total=${u.totalTokenCount()}" }
            }
        }
    }
}