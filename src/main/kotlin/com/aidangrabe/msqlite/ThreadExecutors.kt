package com.aidangrabe.msqlite

import javafx.application.Platform
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
 */
object ThreadExecutors {

    val mainThreadExecutor = Executor { command -> Platform.runLater(command) }

    val workerThreadExecutor = Executors.newFixedThreadPool(2)

}

fun workerThread(work: () -> Unit) {
    ThreadExecutors.workerThreadExecutor.execute(work)
}

fun mainThread(work: () -> Unit) {
    ThreadExecutors.mainThreadExecutor.execute(work)
}