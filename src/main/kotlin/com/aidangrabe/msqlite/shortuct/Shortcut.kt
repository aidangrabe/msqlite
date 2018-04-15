package com.aidangrabe.msqlite.shortuct

import javafx.scene.input.KeyCombination

/** Simple data class for holding information about a shortcut */
data class Shortcut(val combination: KeyCombination, val action: () -> Unit)