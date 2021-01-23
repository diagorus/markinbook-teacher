package com.fuh.markinbook

import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.fuh.markinbook.data.Discipline
import com.fuh.markinbook.data.Group
import react.*
import react.dom.div

external interface ChooseDisciplineDialogProps : DialogProps {
    var disciplines: List<Discipline>
    var onDisciplineClick: (Discipline) -> Unit
}

val chooseDisciplineDialog = functionalComponent<ChooseDisciplineDialogProps> { props ->
    mDialog(
        true,
        scroll = DialogScroll.paper,
        onClose = { _, _ -> props.onClose() }
    ) {
        mDialogTitle("Виберіть дисципліну")
        mDialogContent(true) {
            div {
                mList {
                    props.disciplines.forEach { group ->
                        mListItem(
                            button = true,
                            onClick = {
                                props.onDisciplineClick(group)
                                props.onClose()
                            }
                        ) {
                            mListItemText(group.title)
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.chooseDisciplineDialog(handler: ChooseDisciplineDialogProps.() -> Unit): ReactElement {
    return child(chooseDisciplineDialog) {
        attrs(handler)
    }
}