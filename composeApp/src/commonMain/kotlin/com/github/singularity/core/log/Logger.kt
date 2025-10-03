package com.github.singularity.core.log

import kotlinx.coroutines.flow.Flow

interface Logger {

    fun i(tag: String?, message: String?)

    fun e(tag: String?, message: String?, throwable: Throwable? = null)

    suspend fun clearLog()

    val logStream: Flow<String>

}
