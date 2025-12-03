package com.github.singularity.core.log

import kotlinx.coroutines.flow.Flow

interface Logger {

	fun i(message: String?)

	fun e(message: String?, throwable: Throwable? = null)

    suspend fun clearLog()

    val log: Flow<String>

}
