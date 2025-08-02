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
        val workoutCreationAiService = WorkoutCreationAiServiceFactory.create(jsonLlm)
        return workoutCreationAiService.createWorkout(workoutInformation)
    }
}