package com.aidangrabe.msqlite.tabs.home

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.android.AndroidPackage
import com.aidangrabe.msqlite.android.Device
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import javafx.scene.control.ComboBox
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import tornadofx.*

/**
 *
 */
class HomeTab(private val controller: HomeController) : Tab("Home") {

    val databaseNameField: TextField
    val devicesComboBox: ComboBox<Device>
    val packageComboBox: ComboBox<AndroidPackage>

    val tables = arrayListOf<String>().observable()

    init {

        isClosable = false

        databaseNameField = databaseNameField()
        devicesComboBox = combobox()
        packageComboBox = combobox()

        content = vbox {

            hbox {

                add(packageComboBox)
                add(databaseNameField)

                add(devicesComboBox)

                button("New Query") {
                    setOnAction {
                        tabPane.tabs.add(QueryTab(QueryController()))
                    }
                }
            }

            label("Tables")

            listview(tables) {
                onDoubleClick {
                    controller.onItemClicked(selectionModel.selectedItem)
                }
            }
        }

        controller.onViewAttached(this)
    }

    private fun databaseNameField() = textfield {
        promptText = "Example.db"
        text = Prefs.databaseName
        focusedProperty().addListener { obs, old, new -> controller.onDatabaseNameFieldFocusChanged(new) }
    }

}