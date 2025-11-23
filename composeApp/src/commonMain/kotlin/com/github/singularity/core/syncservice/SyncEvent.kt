package com.github.singularity.core.syncservice

import kotlinx.serialization.Serializable

interface SyncEvent {
	val pluginName: String
}

@Serializable
sealed class Test : SyncEvent {
    override val pluginName = "test"

    @Serializable
    data class TestEvent(
        val c: Int,
    ) : Test()

}
