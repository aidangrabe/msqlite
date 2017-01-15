package com.aidangrabe.msqlite.tabs.query

import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import tornadofx.*

/**
 *
 */
class QueryTab(val controller: QueryController) : Tab("Query") {

    val tableView: TableView<List<String>> = TableView()
    var queryField: TextArea by singleAssign()

    init {

        content = vbox {
            queryField = textarea()
            button("Send") {
                setOnAction {
                    controller.onQueryButtonPressed(queryField.text)
                }
            }
            add(tableView)
        }

        controller.onViewAttached(this)
    }

}