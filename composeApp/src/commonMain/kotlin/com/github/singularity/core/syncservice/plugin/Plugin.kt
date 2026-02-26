package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardOptions
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.reflect.KClass

interface Plugin {

    val eventClass: KClass<*>

    fun handleEvent(event: SyncEvent)

}

@Polymorphic
interface PluginOptions {
    val isEnabled: Boolean

    fun withEnabled(enabled: Boolean): PluginOptions
}

private val pluginOptionsSerializerModule = SerializersModule {
    polymorphic(PluginOptions::class) {
        subclass(ClipboardOptions::class)
    }
}

val pluginOptionsJson = Json {
    serializersModule = pluginOptionsSerializerModule
    prettyPrint = true
    ignoreUnknownKeys = true
    allowTrailingComma = true
}
