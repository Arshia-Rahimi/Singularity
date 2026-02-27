package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.events.SyncEvent
import kotlin.reflect.KClass

interface Plugin {

    val eventClass: KClass<out SyncEvent>

    fun handleEvent(event: SyncEvent)

}
