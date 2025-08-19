package com.github.singularity.core.datastore

import com.github.singularity.core.shared.AppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Settings(
    val theme: AppTheme = AppTheme.System,
)

object SettingsSerializer {
    const val KEY = "settings"

    fun serialize(value: Settings): String =
        Json.encodeToString(value)

    fun deserialize(raw: String?): Settings =
        raw?.let { Json.decodeFromString(it) } ?: Settings()
}
