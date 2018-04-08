package com.aidangrabe.msqlite.android

import java.io.IOException
import java.nio.file.Paths

/**
 *
 */
object Adb {

    val isInstalled by lazy { adbAvailable() }

    var currentDevice: Device? = null

    fun listDevices() = parseDeviceList(exec("devices"))

    fun listDatabasesForPackage(androidPackage: AndroidPackage): List<String> {
        return exec("shell", "run-as", androidPackage.name, "find", ".", "-iname", "*.db")
                .split("[\\r\\n]+")
                .map { Paths.get(it).fileName.toString() }
    }

    fun listPackages(): List<AndroidPackage> {
        val packageNameRegex = """package:(.*)""".toRegex()

        val packages = exec("shell", "pm", "list", "packages")
        return packageNameRegex.findAll(packages)
                .mapNotNull { it.groups[1]?.value }
                .map { AndroidPackage(it) }
                .toList()
    }

    fun exec(vararg command: String): String {
        if (!isInstalled) {
            throw AdbNotInstalledException()
        }

        val options = arrayListOf<String>()
        currentDevice?.let {
            with(options) {
                add("-s")
                add(it.name)
            }
        }

        val commandToExecute = arrayOf("adb") + options + command

        Runtime.getRuntime().exec(commandToExecute).let {
            return it.inputStream.bufferedReader().use { it.readText() }.apply {
                it.waitFor()
            }
        }
    }

    private fun adbAvailable(): Boolean {
        return try {
            Runtime.getRuntime().exec("adb").waitFor()
            true
        } catch (error: IOException) {
            false
        }
    }

    private fun parseDeviceList(deviceText: String): List<Device> {
        val regex = """([\w]+-\d+)\s+(\w+)""".toRegex()

        return regex.findAll(deviceText)
                .map {
                    val name = it.groups[1]?.value
                    val type = it.groups[2]?.value
                    if (name != null && type != null) {
                        Device(name, type)
                    } else {
                        null
                    }
                }
                .filterNotNull()
                .toList()

    }
}

class AdbNotInstalledException : Exception("adb command is not installed. Make sure the Android tools are installed and on your path")