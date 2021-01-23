package com.fuh.markinbook

import react.RProps

external interface DialogProps : RProps {
    var onClose: () -> Unit
}