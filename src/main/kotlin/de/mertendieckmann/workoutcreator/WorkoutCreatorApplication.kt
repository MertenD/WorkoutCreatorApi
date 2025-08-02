package de.mertendieckmann.workoutcreator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkoutCreatorApplication

fun main(args: Array<String>) {
    runApplication<WorkoutCreatorApplication>(*args)
}
