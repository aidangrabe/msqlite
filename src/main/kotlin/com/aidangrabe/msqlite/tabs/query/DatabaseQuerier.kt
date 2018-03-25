package com.aidangrabe.msqlite.tabs.query

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.SqliteApi
import com.aidangrabe.msqlite.mainThread
import com.aidangrabe.msqlite.model.Table
import com.aidangrabe.msqlite.workerThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

/**
 * class that queries the database
 */
class DatabaseQuerier {

    fun query(queryString: String, callback: (Table) -> Unit) {

        workerThread {
            val sqlite = SqliteApi(Prefs.packageName, Prefs.databaseName)
            val output = parseOutput(sqlite.exec(queryString))

            mainThread {
                callback(output)
            }
        }

    }

    private fun parseOutput(output: String): Table {
        val errorMessagePrefix = "Error: "
        if (output.startsWith(errorMessagePrefix)) {
            return Table(listOf("Error"), listOf(listOf(output.substring(errorMessagePrefix.length))))
        }

        val document = Jsoup.parse("<html><body><table>$output</table></body></html>")
        return Table(parseColumnNamesFromOutput(document), parseRowsFromOutput(document))
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