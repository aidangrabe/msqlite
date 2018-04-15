package com.aidangrabe.msqlite

import com.aidangrabe.msqlite.shortuct.ShortcutHandler
import com.aidangrabe.msqlite.tabs.home.HomeController
import com.aidangrabe.msqlite.tabs.home.HomeTab
import com.aidangrabe.msqlite.tabs.query.QueryController
import com.aidangrabe.msqlite.tabs.query.QueryTab
import javafx.event.EventHandler
import javafx.scene.control.TabPane
import javafx.scene.input.KeyEvent
import tornadofx.App
import tornadofx.View
import tornadofx.tabpane
import tornadofx.vbox

/** The Main application class */
class MsqliteApp : App(MainView::class) {

    override fun stop() {
        ThreadExecutors.workerThreadExecutor.shutdownNow()
    }

}

/** The main view for the application */
class MainView : View() {

    private val queryController: QueryController by inject()
    private val homeController: HomeController by inject()
    private val shortcutHandler = ShortcutHandler()

    private lateinit var topTabPane: TabPane

    override val root = vbox {

        topTabPane = tabpane {

            with(tabs) {
                add(HomeTab(homeController))
                add(QueryTab(queryController))
            }

        }

        registerShortcuts()

        onKeyPressed = EventHandler<KeyEvent> {
            shortcutHandler.handleShortcut(it)
        }

    }

    private fun registerShortcuts() = with(shortcutHandler) {
        registerShortcut("Meta+shift+]") { nextTab() }
        registerShortcut("Meta+shift+[") { previousTab() }
        registerShortcut("Meta+w") { closeTab() }
        registerShortcut("Meta+t") { newTab() }
    }

    private fun selectedTabIndex() = topTabPane.selectionModel.selectedIndex

    private fun previousTab() {
        topTabPane.selectionModel.select(selectedTabIndex() - 1)
    }

    private fun nextTab() {
        topTabPane.selectionModel.select(selectedTabIndex() + 1)
    }

    private fun newTab() {
        topTabPane.tabs.add(QueryTab(queryController))
        topTabPane.selectionModel.select(topTabPane.tabs.size - 1)
    }

    private fun closeTab() {
        val selectedTabIndex = selectedTabIndex()

        // prevent closing the Home tab
        if (selectedTabIndex != 0) {
            topTabPane.tabs.remove(topTabPane.tabs[selectedTabIndex])
        }
    }

}