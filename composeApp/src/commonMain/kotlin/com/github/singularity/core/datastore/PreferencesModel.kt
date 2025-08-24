@file:OptIn(ExperimentalUuidApi::class)

package com.github.singularity.core.datastore

import com.github.singularity.core.shared.AppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class PreferencesModel(
    val theme: AppTheme = AppTheme.System,
    val deviceId: String = Uuid.random().toString(),
)

object DataStoreModelSerializer {
    const val KEY = "preferences"
    
    fun serialize(value: PreferencesModel): String =
        Json.encodeToString(value)
    
    fun deserialize(raw: String?): PreferencesModel =
        raw?.let { Json.decodeFromString(it) } ?: PreferencesModel()
    
}
