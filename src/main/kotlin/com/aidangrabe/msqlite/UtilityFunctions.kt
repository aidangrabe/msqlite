package com.aidangrabe.msqlite

import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

/**
 * Some utility functions
 */
fun setClipboardText(text: String) {
    Clipboard.getSystemClipboard().setContent(ClipboardContent().apply {
        putString(text)
    })
}
