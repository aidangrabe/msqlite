package com.aidangrabe.msqlite.android

import java.io.IOException

import kotlin.collections.List

/**
 *
 */
object Adb {

    val isInstalled by lazy { adbAvailable() }

    fun listDevices() = parseDeviceList(exec("devices"))

    fun exec(vararg command: String): String {
        if (!isInstalled) {
            throw AdbNotInstalledException()
        }

        val process = Runtime.getRuntime()
                .exec(arrayOf("adb") + command)
        val output = process.inputStream.bufferedReader().use { it.readText() }
        process.waitFor()
        return output
    }

    private fun adbAvailable(): Boolean {
        try {
            Runtime.getRuntime().exec("adb").waitFor()
            return true
        } catch (error: IOException) {
            return false
        }
    }

    private fun parseDeviceList(deviceText: String): List<Device> {
        val regex = """([\w]+-\d+)\s+(\w+)""".toRegex()

        return regex.findAll(deviceText).map {
            val name = it.groups[1]?.value
            val type = it.groups[2]?.value
            if (name != null && type != null) {
                Device(name, type)
            } else {
                null
            }
        }.filterNotNull()
        .toList()

    }

}

class AdbNotInstalledException : Exception("adb command is not installed. Make sure the Android tools are installed and on your path")