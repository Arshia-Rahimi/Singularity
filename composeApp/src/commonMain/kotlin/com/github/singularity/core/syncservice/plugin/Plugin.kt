package com.github.singularity.core.syncservice.plugin

import kotlin.reflect.KClass

interface Plugin {

	val eventClass: KClass<*>

	fun handleEvent(event: SyncEvent)

}
