package com.fuh.markinbook.data

import kotlinx.serialization.Serializable

@Serializable
data class Mark(val studentId: Int, val value: Int)