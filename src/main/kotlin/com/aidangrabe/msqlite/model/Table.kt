package com.aidangrabe.msqlite.model

import com.aidangrabe.msqlite.setClipboardText
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.ContextMenu
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import tornadofx.action
import tornadofx.item
import java.util.*

/**
 *
 */
data class Table(private val columnNames: List<String>, private val rows: List<List<String>>) {

    private fun addColumnsToTableView(tableView: TableView<List<String>>) {
        val map = HashMap<String, Int>()
        var colNumber = 0
        columnNames.forEach {
            val col = TableColumn<List<String>, String>(it)

            map[it] = colNumber++

            col.cellFactory = cellFactory()
            col.cellValueFactory = cellValueFactory(it, map)

            tableView.columns.add(col)
        }
    }

    private fun cellValueFactory(colName: String, map: HashMap<String, Int>): Callback<TableColumn.CellDataFeatures<List<String>, String>?, ObservableValue<String>?> {
        return Callback {
            val colIndex = map[colName] ?: 0
            var value = "<no value>"

            it?.value?.let {
                if (colIndex < it.size) {
                    value = it[colIndex]
                }
            }
            SimpleStringProperty(value)
        }
    }

    private fun cellFactory(): Callback<TableColumn<List<String>, String>, TableCell<List<String>, String>> {
        return Callback {
            val cell: TableCell<List<String>, String> = TextFieldTableCell()
            cell.contextMenu = ContextMenu().apply {
                item("Copy").action {
                    setClipboardText(cell.text)
                }
            }
            cell
        }
    }

    private fun addDataToTableView(tableView: TableView<List<String>>) {
        tableView.items = FXCollections.observableArrayList(rows)
    }

    fun setupWithTableView(tableView: TableView<List<String>>) {
        tableView.let {
            addColumnsToTableView(it)
            addDataToTableView(it)
        }
    }

}