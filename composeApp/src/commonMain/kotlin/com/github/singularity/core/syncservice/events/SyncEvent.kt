package com.github.singularity.core.syncservice.events

import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardEvent
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Polymorphic
interface SyncEvent

private val syncEventSerializerModule = SerializersModule {
    polymorphic(SyncEvent::class) {
        subclass(ClipboardEvent.Copied::class)
        subclass(ClipboardEvent.SendToClipboard::class)
    }
}

val syncEventJson = Json {
    serializersModule = syncEventSerializerModule
    prettyPrint = true
    ignoreUnknownKeys = true
    allowTrailingComma = true
}
