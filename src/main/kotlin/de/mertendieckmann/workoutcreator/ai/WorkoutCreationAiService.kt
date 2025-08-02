package de.mertendieckmann.workoutcreator.ai

import de.mertendieckmann.workoutcreator.model.WorkoutInformation
import dev.langchain4j.model.output.structured.Description
import dev.langchain4j.service.UserMessage

interface WorkoutCreationAiService {

    fun createWorkout(@UserMessage workoutInformation: WorkoutInformation) : Workout

    data class Workout(
        @Description("The name of the workout")
        val name: String,
        @Description("The description of the workout")
        val description: String,
        @Description("A List of exercises included in the workout")
        val exercises: List<Exercise>
    ) {
        data class Exercise(
            @Description("The name of the exercise")
            val name: String,
            @Description("The description of the exercise")
            val description: String,
            @Description("Detailed step by step instructions for performing the exercise")
            val instructions: List<String>,
            @Description("The muscle groups targeted by the exercise")
            val muscleGroups: List<String>,
            @Description("The equipment required for the exercise")
            val equipment: List<String>,
            @Description("The number of sets to perform")
            val sets: Int,
            @Description("The number of repetitions per set")
            val reps: Int
        )
    }
}