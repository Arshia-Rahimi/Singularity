package com.github.singularity.core.log

import kotlinx.coroutines.flow.Flow

class IosLogger : Logger {

    override fun i(tag: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun e(tag: String?, message: String?, throwable: Throwable?) {
        TODO("Not yet implemented")
    }

    override suspend fun clearLog() {
        TODO("Not yet implemented")
    }

    override val log: Flow<String>
        get() = TODO("Not yet implemented")

}
