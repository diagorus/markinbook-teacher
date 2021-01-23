package com.fuh.markinbook.components

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.fuh.markinbook.DialogProps
import com.fuh.markinbook.data.ApiRepository
import com.fuh.markinbook.data.Lesson
import com.fuh.markinbook.data.appScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import react.*
import styled.css
import styled.styledDiv

external interface AddHomeworkDialogProps : DialogProps {
    var lesson: Lesson
}

val addHomeworkDialog = functionalComponent<AddHomeworkDialogProps> { props ->
    mDialog(
        true,
        scroll = DialogScroll.paper,
        onClose = { _, _ -> props.onClose() }
    ) {
        val (loading, setLoading) = useState(false)

        val (homework, setHomework) = useState(props.lesson.homework)

        mDialogTitle("Домашнє завдання")
        mDialogContent(true) {
            if (loading) {
                mLinearProgress()
            }

            if (homework.tasks.isEmpty()) {
                mTypography("Немає домашнього завдання")
            } else {
                homework.tasks.forEachIndexed { index, task ->
                    mTypography("${index + 1}. ${task.description}") {
                        css {
                            paddingTop = 1.spacingUnits
                        }
                    }
                }
            }

            val (adding, setAdding) = useState(false)

            val (newTaskText, setNewTaskText) = useState("")
            if (adding) {
                styledDiv {
                    css {
                        width = 100.pct
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        alignItems = Align.center
                        marginTop = 4.spacingUnits
                    }

                    mTextField(
                        label = "Завдання",
                        type = InputType.text,
                        value = newTaskText,
                        margin = MFormControlMargin.none,
                        onChange = {
                            val value = (it.target as HTMLInputElement).value
                            setNewTaskText(value)
                        },
                        variant = MFormControlVariant.outlined
                    )

                    mButton(
                        "Додати",
                        variant = MButtonVariant.text,
                        color = MColor.primary,
                        onClick = {
                            setAdding(false)
                            appScope.launch {
                                setLoading(true)
                                ApiRepository.addHomeworkTask(props.lesson.id, homework.id, newTaskText)
                                setLoading(false)
                            }
                        }
                    )
                }
            } else {
                val addTitle = if (homework.tasks.isEmpty()) {
                    "Додати завдання"
                } else {
                    "Додати ще завдання"
                }
                mButton(
                    addTitle,
                    variant = MButtonVariant.text,
                    color = MColor.primary,
                    onClick = {
                        setAdding(true)
                    }
                ) {
                    css {
                        marginTop = 4.spacingUnits
                    }
                    attrs.startIcon =
                        mIcon("add", fontSize = MIconFontSize.small, addAsChild = false)
                }
            }
        }
    }
}

fun RBuilder.addHomeworkDialog(handler: AddHomeworkDialogProps.() -> Unit): ReactElement {
    return child(addHomeworkDialog) {
        attrs(handler)
    }
}