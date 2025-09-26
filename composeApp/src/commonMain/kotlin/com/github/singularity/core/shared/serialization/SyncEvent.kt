package com.github.singularity.core.shared.serialization

import com.github.singularity.core.sync.plugin.clipboard.ClipboardPlugin
import com.github.singularity.ui.feature.main.TestEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface SyncEvent {
    val plugin: String
}

val jsonConverter = Json {
    serializersModule = SerializersModule {
        polymorphic(SyncEvent::class) {
            subclass(TestEvent::class)
            subclass(ClipboardPlugin.Events.Copied::class)
        }
    }
}
