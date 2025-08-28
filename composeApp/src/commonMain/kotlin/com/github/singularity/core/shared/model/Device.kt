package com.github.singularity.core.shared.model

import com.github.singularity.models.Device
import com.github.singularity.models.IServer
import kotlinx.serialization.Serializable

@Serializable
data class LocalServer(
    override val ip: String,
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String,
    override val syncGroupName: String,
    override val syncGroupId: String,
) : Device, IServer
