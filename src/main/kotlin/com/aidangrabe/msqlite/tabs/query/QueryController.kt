package com.aidangrabe.msqlite.tabs.query

import com.aidangrabe.msqlite.android.SqliteApi
import tornadofx.Controller

/**
 *
 */
class QueryController: Controller() {

    private lateinit var view: QueryTab

    fun onViewAttached(view: QueryTab) {
        this.view = view
    }

    fun onQueryButtonPressed(query: String) = execQuery(query)

    fun setQuery(query: String) {
        view.queryField.text = query
        execQuery(query)
    }

    private fun execQuery(query: String) {
        val sqlite = SqliteApi("com.teamwork.chat", "Chat.db")
        val engine = view.webview.engine
        val output = sqlite.exec(query)

        engine.loadContent(htmlTemplate(output))
    }

    private fun htmlTemplate(content: String) = """<html>
        <head>
        </head>
        <body>
            <table>$content</table>
        </body>
    </html>
    """

}