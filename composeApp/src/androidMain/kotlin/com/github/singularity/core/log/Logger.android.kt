package com.github.singularity.core.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.github.singularity.core.shared.LOG_FILE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class AndroidLogger(
    context: Context,
) : Logger {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logFile: File = File(context.filesDir, LOG_FILE)

    override val log = flow {
        while (!logFile.exists()) delay(1000)

        logFile.readLines().joinToString("\n").let { emit(it) }

        BufferedReader(FileReader(logFile)).use { reader ->
            repeat(logFile.readLines().size) {
                reader.readLine()
            }

            while (currentCoroutineContext().isActive) {
                reader.readLine()?.let { emit(it) }
                delay(1000)
            }
        }
    }.runningFold("") { log, newLine ->
        log + newLine
    }.flowOn(Dispatchers.IO)

    init {
        scope.launch {
            createLogFile()
        }
    }

    override fun i(tag: String?, message: String?) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message\n")
        }
        Log.i("log-$tag", message ?: "")
    }

    override fun e(tag: String?, message: String?, throwable: Throwable?) {
        scope.launch {
            logFile.appendText("tag: $tag, message: $message, throwable: { cause: ${throwable?.cause}, errorMessage: ${throwable?.localizedMessage}, e: ${throwable?.stackTrace} }\n")
        }
        Log.e("log-$tag", message, throwable)
    }

    override suspend fun clearLog() {
        logFile.delete()
        createLogFile()
    }

    private fun createLogFile() {
        if (!logFile.createNewFile()) return

        logFile.appendText("log created\n")
        val deviceInfo = mapOf(
            "platform" to "Android",
            "manufacturer" to Build.MANUFACTURER,
            "brand" to Build.BRAND,
            "model" to Build.MODEL,
            "device" to Build.DEVICE,
            "product" to Build.PRODUCT,
            "hardware" to Build.HARDWARE,
            "board" to Build.BOARD,
            "bootloader" to Build.BOOTLOADER,
            "fingerprint" to Build.FINGERPRINT,
            "version_sdk" to Build.VERSION.SDK_INT.toString(),
            "version_release" to Build.VERSION.RELEASE,
            "version_codename" to Build.VERSION.CODENAME,
        )
        logFile.appendText("device info:\n")
        logFile.appendText("$deviceInfo\n")
    }

}
