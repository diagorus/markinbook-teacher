package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
data class School(
    val id: Int = 0,
    val title: String = ""
)