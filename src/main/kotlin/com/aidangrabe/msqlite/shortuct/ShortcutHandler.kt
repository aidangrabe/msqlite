package com.aidangrabe.msqlite.shortuct

import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent

/** Simple class for checking whether a [KeyEvent] matches a list of shortcuts */
class ShortcutHandler {

    private val shortCuts = mutableListOf<Shortcut>()

    fun registerShortcut(combination: String, action: () -> Unit) {
        val keyCombination = KeyCombination.valueOf(combination)
        shortCuts.add(Shortcut(keyCombination, action))
    }

    fun handleShortcut(keyEvent: KeyEvent) {
        shortCuts.forEach {
            if (it.combination.match(keyEvent)) {
                it.action.invoke()
            }
        }
    }

}