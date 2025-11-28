package com.github.singularity.core.syncservice.plugin.clipboard

import kotlinx.coroutines.flow.Flow

interface PlatformClipboardPlugin {

	val systemClipboardUpdatedEvent: Flow<String>

	fun copy(content: String)

}
