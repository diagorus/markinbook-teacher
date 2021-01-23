package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val id: Int,
    val group: Group,
    val discipline: Discipline,
    val start: Long,
    val durationInMinutes: Int,
    val homework: Homework,
    val marks: List<Mark>,
) {
    @Serializable
    data class Homework(val id: Int, val marks: List<Mark>, val tasks: List<Task>) {

        @Serializable
        data class Task(val description: String)
    }
}