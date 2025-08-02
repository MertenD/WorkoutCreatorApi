package de.mertendieckmann.workoutcreator.controller

import de.mertendieckmann.workoutcreator.ai.WorkoutCreationAiService
import de.mertendieckmann.workoutcreator.ai.WorkoutCreationAiServiceFactory
import de.mertendieckmann.workoutcreator.model.WorkoutInformation
import dev.langchain4j.model.chat.ChatModel
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workout")
class WorkoutController(
    @Qualifier("jsonLlm") private val jsonLlm: ChatModel
) {

    @PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createWorkout(
        @RequestBody workoutInformation: WorkoutInformation
    ): WorkoutCreationAiService.Workout {
        if (workoutInformation.description.isBlank() || workoutInformation.muscleGroups.isEmpty()) {
            throw IllegalArgumentException("You must either provide a non blank description or at least one muscle group.")
        }
        if (workoutInformation.description.isNotEmpty() && workoutInformation.description.length > 1000) {
            throw IllegalArgumentException("The description must not be longer than 1000 characters.")
        }
        if (workoutInformation.muscleGroups.size > 10) {
            throw IllegalArgumentException("You must not provide more than 20 muscle groups.")
        }
        if (workoutInformation.equipment.size > 10) {
            throw IllegalArgumentException("You must not provide more than 10 equipment items.")
        }
        if (workoutInformation.muscleGroups.any { it.length > 50 }) {
            throw IllegalArgumentException("Muscle group names must not be longer than 50 characters.")
        }
        if (workoutInformation.equipment.any { it.length > 50 }) {
            throw IllegalArgumentException("Equipment names must not be longer than 50 characters.")
        }

        val workoutCreationAiService = WorkoutCreationAiServiceFactory.create(jsonLlm)
        return workoutCreationAiService.createWorkout(workoutInformation)
    }
}