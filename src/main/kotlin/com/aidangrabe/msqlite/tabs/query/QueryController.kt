package com.aidangrabe.msqlite.tabs.query

import javafx.event.EventHandler
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.Controller

/**
 *
 */
class QueryController : Controller() {

    private lateinit var view: QueryTab
    private val dbQuerier by lazy { DatabaseQuerier() }

    fun onViewAttached(view: QueryTab) {
        this.view = view

        view.queryField.onKeyPressed = EventHandler<KeyEvent> {
            if (it.code == KeyCode.ENTER && it.isMetaDown) {
                execQuery(view.queryField.text)
            }
        }
    }

    fun setQuery(query: String) {
        view.queryField.text = query
        execQuery(query)
    }

    private fun execQuery(query: String) {
        dbQuerier.query(query) {
            view.tableView.columns.clear()
            it.setupWithTableView(view.tableView)
        }

    }

}