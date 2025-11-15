package com.github.singularity.core.shared.model

import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import kotlinx.serialization.Serializable

@Suppress("ArrayInDataClass")
@Serializable
data class PreferencesModel(
    val theme: AppTheme = AppTheme(),
    val deviceId: String = "",
    val appSecret: ByteArray = byteArrayOf(),
    val syncMode: SyncMode = SyncMode.Client,
    val scale: Float = 1f,
)
