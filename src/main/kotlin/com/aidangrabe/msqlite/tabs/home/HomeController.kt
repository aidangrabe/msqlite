package com.aidangrabe.msqlite.tabs.home

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.Adb
import com.aidangrabe.msqlite.android.AndroidPackage
import com.aidangrabe.msqlite.android.Device
import com.aidangrabe.msqlite.android.SqliteApi
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import javafx.collections.FXCollections.observableArrayList
import javafx.util.StringConverter
import tornadofx.Controller
import tornadofx.selectedItem

/**
 *
 */
class HomeController : Controller() {

    private lateinit var view: HomeTab

    fun onViewAttached(view: HomeTab) {
        this.view = view
        reload()

        with(view.packageComboBox) {
            converter = object : StringConverter<AndroidPackage>() {
                override fun fromString(string: String): AndroidPackage = AndroidPackage(string)
                override fun toString(pack: AndroidPackage) = pack.name
            }
            val packages = Adb.listPackages().filterNot {
                it.name.startsWith("com.android") || it.name.startsWith("com.google")
            }
            items = observableArrayList(packages)
            selectionModel.selectFirst()

            valueProperty().addListener { observableValue, old, newValue ->
                val dbs = Adb.listDatabasesForPackage(newValue)
                println(dbs)
                view.databaseNameField.text = dbs.firstOrNull() ?: "none"
                onPackageOrDatabaseNameFocused(false)
            }
        }

        with(view.devicesComboBox) {
            converter = object : StringConverter<Device?>() {
                override fun fromString(string: String?): Device? = null

                override fun toString(device: Device?) = device?.name
            }

            valueProperty().addListener { observableValue, old, newValue ->
                Adb.currentDevice = selectedItem
                reloadTables()
            }
        }
    }

    fun reload() {
        val devices = Adb.listDevices()

        with(view.devicesComboBox) {
            items = observableArrayList(devices)
            selectionModel.selectFirst()
            Adb.currentDevice = selectedItem
        }

        reloadTables()
    }

    fun reloadTables() {
        val sqlite = SqliteApi(Prefs.packageName, Prefs.databaseName)
        var tablesFound = sqlite.listTables().sorted()

        if (tablesFound.isEmpty()) {
            tablesFound = listOf("No tables found in given database")
        }

        view.tables.apply {
            clear()
            addAll(tablesFound)
        }
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
            Prefs.apply {
                packageName = view.packageComboBox.selectedItem?.name ?: ""
                databaseName = view.databaseNameField.text
                save()
            }

            reloadTables()
        }
    }

}