package com.fuh.markinbook.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.accordion.mAccordion
import com.ccfraser.muirwik.components.accordion.mAccordionDetails
import com.ccfraser.muirwik.components.accordion.mAccordionSummary
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.fuh.markinbook.components.ComponentStyles.tableEntryStyle
import com.fuh.markinbook.data.Lesson
import com.fuh.markinbook.putMarkDialog
import com.soywiz.klock.*
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.js.onClickFunction
import react.*
import styled.*

external interface DayScheduleProps : RProps {
    var dayTitle: String
    var daySchedule: List<Lesson>?
}

val daySchedule = functionalComponent<DayScheduleProps> { props ->
    val (showAddHomeworkDialog, setAddHomeworkDialog) = useState<ShowAddHomeworkDialog?>(null)
    val (showPutMarkDialog, setShowPutMarkDialog) = useState<ShowPutMarkDialog?>(null)

    mAccordion {
        mAccordionSummary(expandIcon = mIcon("expand_more", addAsChild = false)) {
            css {
                backgroundColor = Color.lightBlue
                padding(LinearDimension.none)
            }
            mTypography(props.dayTitle, MTypographyVariant.body1)
        }
        mAccordionDetails {
            css {
                padding(0.spacingUnits)
            }

            val daySchedule = props.daySchedule
            if (daySchedule.isNullOrEmpty()) {
                styledDiv {
                    css {
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                        alignItems = Align.center
                        padding(3.spacingUnits)
                    }
                    mTypography("На цей день немає розкладу")
                }
            } else {
                styledTable {
                    css {
                        width = 100.pct
                        borderCollapse = BorderCollapse.collapse
                    }
                    styledTr {
                        styledTh {
                            attrs {
                                text("Оцінки")
                            }
                        }
                        styledTh {
                            attrs {
                                text("Час")
                            }
                        }
                        styledTh {
                            attrs {
                                text("Дисципліна")
                            }
                        }
                        styledTh {
                            attrs {
                                text("Група")
                            }
                        }
                        styledTh {
                            attrs {
                                text("Домашня робота")
                            }
                        }
                    }
                    styledTbody {
                        daySchedule.sortedBy { it.start }.forEachIndexed { index, lesson ->
                            styledTr {
                                css {
                                    backgroundColor = if (index % 2 == 0) {
                                        Color.lightGray
                                    } else {
                                        Color.white
                                    }
                                }
                                styledTd {
                                    css(tableEntryStyle)

                                    val marks = lesson.marks
                                    val title = if (marks.isEmpty()) {
                                        "Немає оцінок"
                                    } else {
                                        "${lesson.marks.count()} оцінок"
                                    }
                                    mButton(
                                        title,
                                        variant = MButtonVariant.text,
                                        onClick = {
                                            setShowPutMarkDialog(ShowPutMarkDialog(false, lesson))
                                        }
                                    ) {
                                        attrs.startIcon =
                                            mIcon("edit-icon", fontSize = MIconFontSize.small, addAsChild = false)
                                    }
                                }

                                val timeFormat = DateFormat("HH:mm")
                                val startTime = timeFormat.format(lesson.start + 2 * 60 * 60 * 1000)
                                val endTime = timeFormat.format(lesson.start + 2 * 60 * 60 * 1000 + (lesson.durationInMinutes * 60 * 1000))
                                styledTd {
                                    css(tableEntryStyle)
                                    attrs {
                                        text("$startTime - $endTime")
                                    }
                                }

                                styledTd {
                                    css(tableEntryStyle)
                                    attrs {
                                        text(lesson.discipline.title)
                                    }
                                }
                                styledTd {
                                    css(tableEntryStyle)
                                    attrs {
                                        text(lesson.group.title)
                                    }
                                }
                                styledTd {
                                    css(tableEntryStyle)

                                    val tasks = lesson.homework.tasks
                                    val homeworkTitle = if (tasks.isEmpty()) {
                                        "Немає домашнього завдання"
                                    } else {
                                        "${tasks.count()} завдань"
                                    }
                                    mButton(
                                        homeworkTitle,
                                        variant = MButtonVariant.text,
                                        color = MColor.primary,
                                        onClick = {
                                            setAddHomeworkDialog(ShowAddHomeworkDialog(lesson))
                                        }
                                    ) {
                                        attrs.startIcon =
                                            mIcon("edit-icon", fontSize = MIconFontSize.small, addAsChild = false)
                                    }

                                    if (!tasks.isNullOrEmpty()) {
                                        mButton(
                                            "Оцінити",
                                            variant = MButtonVariant.text,
                                            color = MColor.primary,
                                            onClick = {
                                                setShowPutMarkDialog(ShowPutMarkDialog(true, lesson))
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPutMarkDialog != null) {
        putMarkDialog {
            onClose = {
                setShowPutMarkDialog(null)
            }
            isHomework = showPutMarkDialog.isHomework
            lesson = showPutMarkDialog.lesson
        }
    }
    if (showAddHomeworkDialog != null) {
        addHomeworkDialog {
            onClose = {
                setAddHomeworkDialog(null)
            }
            this.lesson = showAddHomeworkDialog.lesson
        }
    }
}

private object ComponentStyles : StyleSheet("ComponentStyles", isStatic = true) {
    val tableEntryStyle by css {
        textAlign = TextAlign.center
        verticalAlign = VerticalAlign.middle
    }
}

class ShowAddHomeworkDialog(val lesson: Lesson)
class ShowPutMarkDialog(val isHomework: Boolean, val lesson: Lesson)

fun RBuilder.daySchedule(handler: DayScheduleProps.() -> Unit): ReactElement {
    return child(daySchedule) {
        attrs(handler)
    }
}