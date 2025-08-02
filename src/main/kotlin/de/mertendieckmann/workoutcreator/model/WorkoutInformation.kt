package de.mertendieckmann.workoutcreator.model

data class WorkoutInformation(
    val muscleGroups: List<String>,
    val equipment: List<String>,
    val description: String,
    val exerciseAmount: Int?,
)