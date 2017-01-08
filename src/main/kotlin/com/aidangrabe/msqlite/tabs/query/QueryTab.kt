package com.aidangrabe.msqlite.tabs.query

import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.web.WebView
import tornadofx.*

/**
 *
 */
class QueryTab(val controller: QueryController): Tab("Query") {

    var webview: WebView by singleAssign()
    var queryField: TextArea by singleAssign()

    init {
        content = vbox {
            queryField = textarea()
            button("Send") {
                setOnAction {
                    controller.onQueryButtonPressed(queryField.text)
                }
            }
            webview = webview()
        }

        controller.onViewAttached(this)
    }

}