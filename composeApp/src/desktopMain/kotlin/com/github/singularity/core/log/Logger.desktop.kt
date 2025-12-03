package com.github.singularity.core.log

import com.github.singularity.core.shared.LOG_FILE
import com.github.singularity.core.shared.os
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
import net.harawata.appdirs.AppDirsFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class DesktopLogger : Logger {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
	private val directory = AppDirsFactory.getInstance()
		.getUserDataDir("Singularity", null, null)
	private val logFile: File = File(directory, LOG_FILE)

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
	}

	override fun e(message: String?, throwable: Throwable?) {
		scope.launch {
			logFile.appendText("time: ${LocalDateTime.now()}, message: $message, throwable: { cause: ${throwable?.cause}, errorMessage: ${throwable?.localizedMessage}, e: ${throwable?.stackTrace} }\n")
		}
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
			"platform" to "Desktop",
			"os" to os,
		)
		logFile.appendText("device info:\n")
		logFile.appendText("$deviceInfo\n")
	}

}
