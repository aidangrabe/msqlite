package com.aidangrabe.msqlite.tabs.home

import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import javafx.scene.control.Tab
import tornadofx.*

/**
 *
 */
class HomeTab(val controller: HomeController): Tab("Home") {

    val tables = arrayListOf<String>().observable()

    init {

        controller.onViewAttached(this)

        isClosable = false

        content = vbox {

            hbox {
                textfield {
                    this.promptText = "com.example.org"
                }
                textfield {
                    this.promptText = "ExampleDb.db"
                }
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

}