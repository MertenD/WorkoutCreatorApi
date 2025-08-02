package de.mertendieckmann.workoutcreator.model

import dev.langchain4j.model.output.structured.Description

data class WorkoutInformation(
    @Description("The target muscle groups for the workout")
    val muscleGroups: List<String>,
    @Description("The equipment that can (but does not have to) be used for the workout")
    val equipment: List<String>,
    @Description("A brief description of the workout, e.g. \"A full body workout for beginners\"")
    val description: String
)