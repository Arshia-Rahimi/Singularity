package com.github.singularity.core.shared

import java.io.File

actual val platform = "Desktop"

actual val os: String
    get() {
        val osName = System.getProperty("os.name").lowercase()

        return when {
            osName.contains("linux") -> {
                // Try reading /etc/os-release for Linux distros
                val file = File("/etc/os-release")
                if (file.exists()) {
                    val map = file.readLines()
                        .mapNotNull { line ->
                            line.split("=").takeIf { it.size == 2 }
                        }
                        .associate { it[0] to it[1].trim('"') }
                    map["PRETTY_NAME"] ?: "Linux"
                } else {
                    "Linux"
                }
            }

            osName.contains("mac") -> {
                try {
                    val version = ProcessBuilder("sw_vers", "-productVersion")
                        .start()
                        .inputStream.bufferedReader().readText().trim()
                    "macOS $version"
                } catch (e: Exception) {
                    "macOS"
                }
            }

            osName.contains("win") -> {
                try {
                    val caption = ProcessBuilder("wmic", "os", "get", "Caption")
                        .redirectErrorStream(true)
                        .start()
                        .inputStream.bufferedReader().readText()
                        .lines()
                        .drop(1) // skip header
                        .firstOrNull { it.isNotBlank() }
                        ?.trim()
                    caption ?: "Windows"
                } catch (e: Exception) {
                    "Windows"
                }
            }

            else -> "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
        }
    }
