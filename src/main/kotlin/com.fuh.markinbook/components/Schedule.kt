package com.fuh.markinbook.components

import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.spacingUnits
import com.fuh.markinbook.data.*
import com.soywiz.klock.DateTime
import com.soywiz.klock.weekOfYear0
import com.soywiz.klock.weekOfYear1
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.router.dom.useHistory
import styled.css
import styled.styledDiv

external interface ScheduleProps : RProps {
    var showLoading: (show: Boolean) -> Unit
}

val schedule = functionalComponent<ScheduleProps> {
    val (schedule, setSchedule) = useState(emptyMap<Day, List<Lesson>>())
    useEffect(emptyList()) {
        appScope.launch {
            val today = DateTime.now()
            val newSchedule = ApiRepository.lessonsByDays(today.weekOfYear1.toString(), today.yearInt.toString())
            setSchedule(newSchedule)
        }
    }
    val history = useHistory()

    styledDiv {
        css {
            paddingLeft = 2.spacingUnits
            paddingRight = 2.spacingUnits
        }

        daySchedule {
            dayTitle = "Понеділок"
            daySchedule = schedule[Day.MONDAY]
        }
        daySchedule {
            dayTitle = "Вівторок"
            daySchedule = schedule[Day.TUESDAY]
        }
        daySchedule {
            dayTitle = "Середа"
            daySchedule = schedule[Day.WEDNESDAY]
        }
        daySchedule {
            dayTitle = "Четвер"
            daySchedule = schedule[Day.THURSDAY]
        }
        daySchedule {
            dayTitle = "П'ятниця"
            daySchedule = schedule[Day.FRIDAY]
        }
        daySchedule {
            dayTitle = "Субота"
            daySchedule = schedule[Day.SATURDAY]
        }
        daySchedule {
            dayTitle = "Неділя"
            daySchedule = schedule[Day.SUNDAY]
        }
    }

    mFab(
        "add",
        MColor.primary,
        size = MButtonSize.large,
        onClick = {
            history.push("/add-lesson")
        }
    ) {
        css {
            position = Position.fixed
            bottom = 4.spacingUnits
            right = 4.spacingUnits
        }
    }
}

fun RBuilder.schedule(handler: ScheduleProps.() -> Unit): ReactElement {
    return child(schedule) {
        attrs(handler)
    }
}