@file:OptIn(ExperimentalUuidApi::class)

package com.github.singularity.core.datastore

import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.SyncMode
import kotlinx.serialization.Serializable
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
        .let { Base64.withPadding(Base64.PaddingOption.ABSENT).encodeToByteArray(it) },
    val syncMode: SyncMode = SyncMode.Client,
) 
