package com.fuh.markinbook.data

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

object SessionManager {

    private const val KEY_TOKEN = "token"

    var token: String = localStorage[KEY_TOKEN] ?: ""
        set(value) {
            field = value
            localStorage[KEY_TOKEN] = value
        }

    var teacher: Teacher? = null
}