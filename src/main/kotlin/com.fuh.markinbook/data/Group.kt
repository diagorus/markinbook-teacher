package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
class Group(val id: Int, val title: String, val students: List<Student>)