@file:OptIn(ExperimentalUuidApi::class)

package com.github.singularity.core.datastore

import com.github.singularity.core.shared.AppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("ArrayInDataClass")
@Serializable
data class PreferencesModel(
    val theme: AppTheme = AppTheme.System,
    val deviceId: String = Uuid.random().toString(),
    val appSecret: ByteArray = Random.Default.nextBytes(32)
        .let { Base64.withPadding(Base64.PaddingOption.ABSENT).encodeToByteArray(it) }
) 

object DataStoreModelSerializer {
    const val KEY = "preferences"

    fun serialize(value: PreferencesModel): String =
        Json.encodeToString(value)

    fun deserialize(raw: String?): PreferencesModel =
        raw?.let { Json.decodeFromString(it) } ?: PreferencesModel()

}
