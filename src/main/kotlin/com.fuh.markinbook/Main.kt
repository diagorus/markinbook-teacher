package com.fuh.markinbook

import kotlinx.browser.document
import react.dom.render

fun main() {
    render(document.getElementById("root")) {
        app()
    }
}