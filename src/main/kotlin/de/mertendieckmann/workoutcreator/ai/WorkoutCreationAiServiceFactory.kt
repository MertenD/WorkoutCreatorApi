package de.mertendieckmann.workoutcreator.ai

import de.mertendieckmann.workoutcreator.model.MuscleGroup
import dev.langchain4j.model.chat.ChatModel
import dev.langchain4j.model.input.PromptTemplate
import dev.langchain4j.service.AiServices

object WorkoutCreationAiServiceFactory {

    fun create(jsonLlm: ChatModel): WorkoutCreationAiService {
        return AiServices
            .builder(WorkoutCreationAiService::class.java)
            .chatModel(jsonLlm)
            .systemMessageProvider {getPromptTemplate().template() }
            .build()
    }

    private fun getPromptTemplate(): PromptTemplate {
        return PromptTemplate.from(buildString {
            appendLine(
                """
                    You are an expert in creating workout plans. You must return a structured workout plan based on the provided muscle groups, equipment, and description.
                    Use all available information and context to create a comprehensive workout plan that includes exercises, their descriptions, instructions, targeted muscle groups, and required equipment. 
                    
                    The response should be structured as follows:
                    - name: The name of the workout
                    - description: A brief description of the workout
                    - exercises: A list of exercises included in the workout, each with:
                        - name: The name of the exercise
                        - description: A brief description of the exercise
                        - instructions: Detailed step-by-step instructions for performing the exercise
                        - muscleGroups: The muscle groups targeted by the exercise
                        - equipment: The equipment required for the exercise
                        - sets: The number of sets to perform
                        - reps: The number of repetitions per set
                    
                    Ensure that the workout plan is coherent and practical based on the provided inputs.
                    
                    Make sure to use and follow all given information from the user message to create a detailed and structured workout plan.
                    If the users workout description contains any information about the exercise amount, use it, but you must always limit the number of exercises to a maximum of 10.
                    
                    Here are all possible muscle groups that can be used in the workout plan. Always use these names for the muscle groups and do not use any other names:
                    ${MuscleGroup.entries.joinToString(separator = ", ") { it.name.lowercase() }}
                """.trimIndent()
            )
        })
    }
}