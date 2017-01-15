package com.aidangrabe.msqlite.tabs.home

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.SqliteApi
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import tornadofx.Controller

/**
 *
 */
class HomeController : Controller() {

    private lateinit var view: HomeTab

    fun onViewAttached(view: HomeTab) {
        this.view = view
        reload()
    }

    fun reload() {

        val sqlite = SqliteApi("com.teamwork.chat", "Chat.db")
        view.tables.addAll(sqlite.listTables().sorted())

    }

    fun onItemClicked(tableName: String) {
        val queryController = QueryController()
        val queryTab = QueryTab(queryController)
        queryTab.text = tableName

        view.tabPane.tabs.add(queryTab)
        view.tabPane.selectionModel.select(queryTab)
        queryController.setQuery("SELECT * FROM $tableName")
    }

    fun onPackageNameFieldFocusChanged(focused: Boolean) = onPackageOrDatabaseNameFocused(focused)
    fun onDatabaseNameFieldFocusChanged(focused: Boolean) = onPackageOrDatabaseNameFocused(focused)

    private fun onPackageOrDatabaseNameFocused(focused: Boolean) {
        if (!focused) {
            Prefs.packageName = view.packageNameField.text
            Prefs.databaseName = view.databaseNameField.text
            Prefs.save()
        }
    }

}