package com.aidangrabe.msqlite.tabs.home

import com.aidangrabe.msqlite.Prefs
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import tornadofx.*

/**
 *
 */
class HomeTab(val controller: HomeController) : Tab("Home") {

    val packageNameField: TextField
    val databaseNameField: TextField

    val tables = arrayListOf<String>().observable()

    init {

        controller.onViewAttached(this)

        isClosable = false

        packageNameField = packageNameField()
        databaseNameField = databaseNameField()

        content = vbox {

            hbox {

                add(packageNameField)
                add(databaseNameField)

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
    }

    private fun packageNameField() = textfield {
        promptText = "com.example"
        text = Prefs.packageName
        focusedProperty().addListener { obs, old, new -> controller.onPackageNameFieldFocusChanged(new) }
    }

    private fun databaseNameField() = textfield {
        promptText = "Example.db"
        text = Prefs.databaseName
        focusedProperty().addListener { obs, old, new -> controller.onDatabaseNameFieldFocusChanged(new) }
    }

}