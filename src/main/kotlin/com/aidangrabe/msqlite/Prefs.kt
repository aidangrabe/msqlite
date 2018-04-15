package com.aidangrabe.msqlite

import tornadofx.JsonBuilder
import java.io.File
import java.io.StringReader
import javax.json.Json

/**
 * User preferences for the application
 */
object Prefs {

    private const val KEY_PACKAGE_NAME = "packageName"
    private const val KEY_DATABASE_NAME = "databaseName"
    private const val pathToPrefsFile = ".msqlite-prefs.json"

    private val prefsFile: File

    var packageName = "com.example"
    var databaseName = "Sample.db"

    init {
        val homeDirectory = File(System.getProperty("user.home"))

        prefsFile = File(homeDirectory, pathToPrefsFile)
        prefsFile.createNewFile()
        readAndParseJson()
    }

    private fun prefsToJson() = JsonBuilder()
            .addIfNotEmpty(KEY_PACKAGE_NAME, packageName)
            .addIfNotEmpty(KEY_DATABASE_NAME, databaseName)
            .build()

    private fun readAndParseJson() {
        val jsonReader = Json.createReader(StringReader(readJsonFromDisk()))
        val jsonPrefs = jsonReader.readObject()

        packageName = jsonPrefs.getString(KEY_PACKAGE_NAME, "")
        databaseName = jsonPrefs.getString(KEY_DATABASE_NAME, "")
    }

    private fun readJsonFromDisk(): String {
        val json = prefsFile.readText()
        return if (json.isEmpty()) "{}" else json
    }

    fun save() {
        prefsFile.writeText(prefsToJson().toString())
    }

    private fun JsonBuilder.addIfNotEmpty(key: String, value: String): JsonBuilder {
        if (value.trim().isNotEmpty()) {
            add(key, value)
        }
        return this
    }

}