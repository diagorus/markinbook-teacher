package com.fuh.markinbook

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.dialog.*
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.fuh.markinbook.data.ApiRepository
import com.fuh.markinbook.data.Lesson
import com.fuh.markinbook.data.appScope
import kotlinx.coroutines.launch
import kotlinx.css.marginLeft
import kotlinx.css.paddingTop
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.div
import styled.css

external interface PutMarkDialogProps : DialogProps {
    var isHomework: Boolean
    var lesson: Lesson
    var onApply: () -> Unit
}

val putMarkDialog = functionalComponent<PutMarkDialogProps> { props ->
    mDialog(
        true,
        scroll = DialogScroll.paper,
        onClose = { _, _ -> props.onClose() }
    ) {
        val title = if (props.isHomework) {
            "Оцінити домашнє завдання"
        } else {
            "Оцінити роботу на занятті"
        }

        val homework = props.lesson.homework
        val isHomeworkAndEmpty = props.isHomework && homework.tasks.isEmpty()

        val marks = if (props.isHomework) {
            props.lesson.homework.marks
        } else {
            props.lesson.marks
        }
            .associate { it.studentId to it.value.toString() }.toMutableMap()
        val (marksErrors, setMarksErrors) = useState<List<Int>>(emptyList())

        val (loading, setLoading) = useState(false)

        mDialogTitle(title)
        mDialogContent(true) {
            if (loading) {
                mLinearProgress()
            }

            if (props.isHomework) {
                if (homework.tasks.isEmpty()) {
                    mTypography("Немає домашннього завдання")
                } else {
                    homework.tasks.forEachIndexed { index, task ->
                        mTypography("${index + 1}. ${task.description}") {
                            css {
                                paddingTop = 1.spacingUnits
                            }
                        }
                    }
                }
            }

            if (!isHomeworkAndEmpty) {
                div {
                    mList {
                        props.lesson.group.students.forEach { student ->
                            mListItem {
                                mListItemText("${student.lastName} ${student.firstName}")

                                val studentId = student.id
                                val hasError = marksErrors.contains(student.id)
                                val errorText = if (hasError) {
                                    "Невірний формат оцінки"
                                } else {
                                    null
                                }
                                val mark = marks[studentId]
                                mTextField(
                                    label = "Оцінка",
                                    error = hasError,
                                    helperText = errorText,
                                    type = InputType.number,
                                    value = mark,
                                    disabled = mark != null,
                                    margin = MFormControlMargin.none,
                                    onChange = {
                                        val value = (it.target as HTMLInputElement).value
                                        marks[studentId] = value
                                    },
                                    variant = MFormControlVariant.outlined
                                ) {
                                    css {
                                        marginLeft = 2.spacingUnits
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        mDialogActions {
            if (!isHomeworkAndEmpty) {
                mButton(
                    "Відміна",
                    MColor.primary,
                    onClick = {
                        props.onClose()
                    }
                )
            }

            val applyTitle = if (isHomeworkAndEmpty) {
                "Ок"
            } else {
                "Застосувати"
            }
            mButton(
                applyTitle,
                MColor.primary,
                onClick = {
                    val newMarksErrors = marks
                        .filter { (_, mark) ->
                            val intMark = mark.toIntOrNull()
                            intMark == null || intMark < 0
                        }
                        .map { (studentId, _) -> studentId }
                    setMarksErrors(newMarksErrors)

                    if (marksErrors.isEmpty()) {
                        setLoading(true)
                        marks.forEach { (studentId, value) ->
                            appScope.launch {
                                if (props.isHomework) {
                                    ApiRepository.addHomeworkMark(
                                        props.lesson.id,
                                        props.lesson.homework.id,
                                        studentId.toString(),
                                        value
                                    )
                                } else {
                                    ApiRepository.addLessonMark(props.lesson.id.toString(), studentId.toString(), value)
                                }
                            }
                        }
                        setLoading(false)
                        props.onClose()
                    }
                }
            )
        }
    }
}

fun RBuilder.putMarkDialog(handler: PutMarkDialogProps.() -> Unit): ReactElement {
    return child(putMarkDialog) {
        attrs(handler)
    }
}