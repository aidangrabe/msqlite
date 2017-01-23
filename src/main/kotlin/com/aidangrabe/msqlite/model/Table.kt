package com.aidangrabe.msqlite.model

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.util.Callback
import java.util.*

/**
 *
 */
data class Table(val columnNames: List<String>, val rows: List<List<String>>) {

    fun addColumnsToTableView(tableView: TableView<List<String>>) {
        val map = HashMap<String, Int>()
        var colNumber = 0
        columnNames.forEach {
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
            tableView.columns.add(col)
        }
    }

    fun addDataToTableView(tableView: TableView<List<String>>) {
        tableView.items = FXCollections.observableArrayList(rows)
    }

    fun setupWithTableView(tableView: TableView<List<String>>) {
        tableView.let {
            addColumnsToTableView(it)
            addDataToTableView(it)
        }
    }

}