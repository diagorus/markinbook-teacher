package com.fuh.markinbook.data

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

object ApiRepository {

    private val httpClient = HttpClient(Js) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            host = "0.0.0.0"
            port = 8081
            header(HttpHeaders.Authorization, "Bearer ${SessionManager.token}")
        }
    }

    private const val BASE_URL = "http://0.0.0.0:8081/api"
    private const val API_VERSION = "v1"
    private const val LOCATION_USERS = "users"
    private const val LOCATION_TEACHERS = "teachers"
    private const val LOCATION_SCHOOLS = "schools"
    private const val LOCATION_GROUPS = "groups"
    private const val LOCATION_DISCIPLINES = "disciplines"
    private const val LOCATION_LESSONS = "lessons"
    private const val LOCATION_HOMEWORKS = "homeworks"

    suspend fun signIn(email: String, password: String): String {
        return httpClient.submitForm(Parameters.build {
            append("email", email)
            append("password", password)
            append("role", "teacher")
        }) {
            url("$BASE_URL/$API_VERSION/$LOCATION_USERS/teacher/signIn")
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        schoolId: Int?,
    ) = httpClient.submitForm<String>(Parameters.build {
        append("email", email)
        append("password", password)
        append("firstName", firstName)
        append("lastName", lastName)
        append("schoolId", schoolId?.toString().orEmpty())
    }) {
        url("$BASE_URL/$API_VERSION/$LOCATION_USERS/teacher/signUp")
    }

    suspend fun currentTeacher() = httpClient.get<Teacher> {
        url("$BASE_URL/$API_VERSION/$LOCATION_TEACHERS/current")
    }

    suspend fun schools() = httpClient.get<List<School>> {
        url("$BASE_URL/$API_VERSION/$LOCATION_SCHOOLS")
    }

    suspend fun groups(schoolId: Int) = httpClient.get<List<Group>> {
        url("$BASE_URL/$API_VERSION/$LOCATION_SCHOOLS/$schoolId/$LOCATION_GROUPS")
    }

    suspend fun disciplines(schoolId: Int) = httpClient.get<List<Discipline>> {
        url("$BASE_URL/$API_VERSION/$LOCATION_SCHOOLS/$schoolId/$LOCATION_DISCIPLINES")
    }

    suspend fun addHomework(lessonId: Int, taskDescription: String) = httpClient.submitForm<Lesson.Homework>(Parameters.build {
        append("taskDescription", taskDescription)
    }) {
        url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/$lessonId/homeworks/add")
    }

    suspend fun addHomeworkTask(lessonId: Int, homeworkId: Int, description: String) = httpClient.submitForm<Lesson.Homework>(Parameters.build {
        append("description", description)
    }) {
        url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/$lessonId/homeworks/$homeworkId/tasks/add")
    }


    suspend fun addLessonMark(
        lessonId: String,
        studentId: String,
        value: String
    ) = httpClient.submitForm<String>(Parameters.build {
        append("studentId", studentId)
        append("value", value)
    }) {
        url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/$lessonId/marks/add")
    }

    suspend fun addHomeworkMark(
        lessonId: Int,
        homeworkId: Int,
        studentId: String,
        value: String
    ) = httpClient.submitForm<String>(Parameters.build {
        append("studentId", studentId)
        append("value", value)
    }) {
        url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/$lessonId/$LOCATION_HOMEWORKS/$homeworkId/marks/add")
    }

    suspend fun lessonsByDays(
        week: String,
        year: String
    ) = httpClient.submitForm<Map<Day, List<Lesson>>>(Parameters.build {
        append("week", week)
        append("year", year)
    }, true) {
        url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/by-days")
    }

    suspend fun addLesson(groupId: String, disciplineId: String, start: String, durationInMinutes: String): Lesson {
        return httpClient.submitForm(Parameters.build {
            append("groupId", groupId)
            append("disciplineId", disciplineId)
            append("start", start)
            append("durationInMinutes", durationInMinutes)
        }) {
            url("$BASE_URL/$API_VERSION/$LOCATION_LESSONS/add")
        }
    }
}