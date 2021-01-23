package com.fuh.markinbook.components

import com.fuh.markinbook.data.ApiRepository
import com.fuh.markinbook.data.SessionManager
import com.fuh.markinbook.data.appScope
import kotlinx.coroutines.launch
import react.*
import react.router.dom.useHistory

val index = functionalComponent<RProps> {
    val history = useHistory()
    useEffect(emptyList()) {
        appScope.launch {
            try {
                val result = ApiRepository.currentTeacher()
                SessionManager.teacher = result
                history.push("/schedule")
            } catch (e: Exception) {
                history.push("/auth")
            }
        }
    }
}

fun RBuilder.index(): ReactElement {
    return child(index)
}