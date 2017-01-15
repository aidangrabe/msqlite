package com.aidangrabe.msqlite.tabs.query

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.SqliteApi
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.control.TableColumn
import javafx.util.Callback
import tornadofx.Controller
import java.util.*
import java.util.regex.Pattern

/**
 *
 */
class QueryController: Controller() {

    private lateinit var view: QueryTab
    private val columnHeadingPattern by lazy { Pattern.compile("<TH>(\\w+)</TH>") }
    private val cellPattern by lazy { Pattern.compile("<TD>(.*)</TD>") }

    fun onViewAttached(view: QueryTab) {
        this.view = view
    }

    fun onQueryButtonPressed(query: String) = execQuery(query)

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

        buildTable(parseColumnNamesFromOutput(output))

        view.tableView.items = observableArrayList(parseRowsFromOutput(output))
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

    private fun parseRowsFromOutput(output: String): List<List<String>> {
        val rowsOfCells = ArrayList<List<String>>()

        val rows = output.split("<TR>").filter {
            it.isNotEmpty() && !it.startsWith("<TH>")
        }

        rows.forEach {
            rowsOfCells.add(parseCellsFromRow(it))
        }

        return rowsOfCells
    }

    private fun parseCellsFromRow(row: String): List<String> {
        return getAllMatchesForGroup(cellPattern, row, 1)
    }

    private fun parseColumnNamesFromOutput(output: String): List<String> {
        return getAllMatchesForGroup(columnHeadingPattern, output, 1)
    }

    private fun getAllMatchesForGroup(pattern: Pattern, input: String, groupNumber: Int): List<String> {
        val matches = ArrayList<String>()
        val matcher = pattern.matcher(input)
        while (matcher.find()) {
            if (matcher.groupCount() > groupNumber - 1) {
                matches.add(matcher.group(groupNumber))
            }
        }
        return matches
    }

}