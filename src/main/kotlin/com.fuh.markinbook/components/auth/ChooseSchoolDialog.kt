package com.fuh.markinbook.components.auth

import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.fuh.markinbook.DialogProps
import com.fuh.markinbook.data.School
import react.*
import react.dom.div

external interface ChooseSchoolDialogProps : DialogProps {
    var schools: List<School>
    var onSchoolClick: (School) -> Unit
}

val chooseSchoolDialog = functionalComponent<ChooseSchoolDialogProps> { props ->
    mDialog(
        true,
        scroll = DialogScroll.paper,
        onClose = { _, _ -> props.onClose() }
    ) {
        mDialogTitle("Виберіть навчальний заклад")
        mDialogContent(true) {
            div {
                mList {
                    props.schools.forEach { school ->
                        mListItem(
                            button = true,
                            onClick = {
                                props.onSchoolClick(school)
                                props.onClose()
                            }
                        ) {
                            mListItemText(school.title)
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.chooseSchoolDialog(handler: ChooseSchoolDialogProps.() -> Unit): ReactElement {
    return child(chooseSchoolDialog) {
        attrs(handler)
    }
}