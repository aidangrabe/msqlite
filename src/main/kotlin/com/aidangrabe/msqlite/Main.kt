package com.aidangrabe.msqlite

import com.aidangrabe.msqlite.tabs.home.HomeController
import com.aidangrabe.msqlite.tabs.home.HomeTab
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import tornadofx.App
import tornadofx.View
import tornadofx.tabpane
import tornadofx.vbox

/**
 *
 */
class MyApp: App(MainView::class)

class MainView : View() {

    val queryController: QueryController by inject()
    val homeController: HomeController by inject()

    override val root = vbox {
        tabpane {
            selectionModel?.selectedItemProperty()?.addListener { observable, oldTab, newTab ->

            }

            with(tabs) {
                add(HomeTab(homeController))
                add(QueryTab(queryController))
            }
        }

    }

}
