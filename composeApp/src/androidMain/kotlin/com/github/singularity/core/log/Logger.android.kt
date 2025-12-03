package com.github.singularity.core.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.github.singularity.core.shared.LOG_FILE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidLogger(
	context: Context,
) : Logger {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
	private val logFile: File = File(context.filesDir, LOG_FILE)

	private val refreshState = MutableSharedFlow<Unit>()

	override val log = refreshState.onStart { emit(Unit) }.flatMapLatest {
		flow {
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
		}
	}.flowOn(Dispatchers.IO)

	init {
		scope.launch {
			createLogFile()
		}
	}

	override fun i(message: String?) {
		scope.launch {
			logFile.appendText("time: ${LocalDateTime.now()}, message: $message\n")
		}
		Log.i("log", message ?: "")
	}

	override fun e(message: String?, throwable: Throwable?) {
		scope.launch {
			logFile.appendText("time: ${LocalDateTime.now()}, message: $message, throwable: { cause: ${throwable?.cause}, errorMessage: ${throwable?.localizedMessage}, e: ${throwable?.stackTrace} }\n")
		}
		Log.e("log", message, throwable)
	}

	override suspend fun clearLog() {
		logFile.delete()
		createLogFile()
		refreshState.emit(Unit)
	}

	private fun createLogFile() {
		if (!logFile.createNewFile()) return

		logFile.appendText("log created\n")
		val deviceInfo = mapOf(
			"time" to LocalDateTime.now(),
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
