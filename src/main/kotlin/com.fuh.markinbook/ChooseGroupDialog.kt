package com.fuh.markinbook

import com.ccfraser.muirwik.components.dialog.DialogScroll
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.fuh.markinbook.data.Group
import react.*
import react.dom.div

external interface ChooseGroupDialogProps : DialogProps {
    var groups: List<Group>
    var onGroupClick: (Group) -> Unit
}

val chooseGroupDialog = functionalComponent<ChooseGroupDialogProps> { props ->
    mDialog(
        true,
        scroll = DialogScroll.paper,
        onClose = { _, _ -> props.onClose() }
    ) {
        mDialogTitle("Виберіть групу")
        mDialogContent(true) {
            div {
                mList {
                    props.groups.forEach { group ->
                        mListItem(
                            button = true,
                            onClick = {
                                props.onGroupClick(group)
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

fun RBuilder.chooseGroupDialog(handler: ChooseGroupDialogProps.() -> Unit): ReactElement {
    return child(chooseGroupDialog) {
        attrs(handler)
    }
}