package com.aidangrabe.msqlite.tabs.query

import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import tornadofx.add
import tornadofx.singleAssign
import tornadofx.textarea
import tornadofx.vbox

/**
 *
 */
class QueryTab(val controller: QueryController) : Tab("Query") {

    val tableView: TableView<List<String>> = TableView()
    var queryField: TextArea by singleAssign()

    init {

        content = vbox {
            queryField = textarea {
                promptText = "CMD + Enter to submit query"
            }
            add(tableView)
        }

        controller.onViewAttached(this)
    }

}