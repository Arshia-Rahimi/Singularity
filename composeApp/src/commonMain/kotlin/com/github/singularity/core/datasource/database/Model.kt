package com.github.singularity.core.datasource.database

import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("ArrayInDataClass")
@Serializable
data class PreferencesModel(
	val theme: AppTheme = AppTheme(),
	val deviceId: String = "",
	val appSecret: ByteArray = byteArrayOf(),
	val syncMode: SyncMode = SyncMode.Client,
	val scale: Float = 1f,
)

@Serializable
data class PluginSettingsModel(
    val name: String,
    val data: PluginSettingsData = emptyMap(),
    val isEnabled: Boolean = true,
)

typealias PluginSettingsData = Map<String, String?>

data class JoinedSyncGroupModel(
	val authToken: String,
	val syncGroupName: String,
	val syncGroupId: String,
	val isDefault: Boolean = false,
)

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroupNodeModel(
	val deviceId: String = Uuid.random().toString(),
	val deviceName: String,
	val deviceOs: String,
	val authToken: String,
	val syncGroupId: String,
	val syncGroupName: String,
)

@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroupModel(
	val name: String,
	val isDefault: Boolean = false,
	val hostedSyncGroupId: String = Uuid.random().toString(),
	val nodes: List<HostedSyncGroupNodeModel> = emptyList(),
)
