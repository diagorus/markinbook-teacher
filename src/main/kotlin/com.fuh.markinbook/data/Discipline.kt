package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
data class Discipline(
    val id: Int = 0,
    val title: String = ""
)