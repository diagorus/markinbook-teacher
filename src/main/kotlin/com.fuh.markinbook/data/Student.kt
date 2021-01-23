package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val schoolId: Int,
    val groupId: Int,
    val profileImage: String?,
)