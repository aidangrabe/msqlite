package com.aidangrabe.msqlite.tabs.query

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.SqliteApi
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections.observableArrayList
import javafx.event.EventHandler
import javafx.scene.control.TableColumn
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.Callback
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import tornadofx.Controller
import java.util.*

/**
 *
 */
class QueryController : Controller() {

    private lateinit var view: QueryTab

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
        val sqlite = SqliteApi(Prefs.packageName, Prefs.databaseName)
        parseOutput(sqlite.exec(query))
    }

    private fun parseOutput(output: String) {
        view.tableView.columns.clear()

        val document = Jsoup.parse("<html><body><table>$output</table></body></html>")
        buildTable(parseColumnNamesFromOutput(document))

        view.tableView.items = observableArrayList(parseRowsFromOutput(document))
    }

    private fun buildTable(columns: List<String>) {
        val map = HashMap<String, Int>()
        var colNumber = 0
        columns.forEach {
            val col = TableColumn<List<String>, String>(it)
            val colName = it

            map.put(it, colNumber++)

            col.cellValueFactory = Callback<TableColumn.CellDataFeatures<List<String>, String>?, ObservableValue<String>?> {
                val colIndex = map[colName] ?: 0
                var value = "<no value>"

                it?.value?.let {
                    if (colIndex < it.size) {
                        value = it[colIndex]
                    }
                }
                SimpleStringProperty(value)
            }
            view.tableView.columns.add(col)
        }
    }

    private fun parseRowsFromOutput(document: Document): List<List<String>> {
        val rowsOfCells = ArrayList<List<String>>()
        document.getElementsByTag("tr")
                .drop(1)    // skip the first row, it's the headers
                .forEach {
                    rowsOfCells.add(ArrayList(it.getElementsByTag("td").map { it.text() }))
                }
        return rowsOfCells
    }

    private fun parseColumnNamesFromOutput(document: Document): List<String> {
        return document.getElementsByTag("th").map { it.text() }
    }

}