package com.github.singularity.core.database.entities

import com.github.singularity.models.Server

data class JoinedSyncGroup(
    val authToken: String,
    val syncGroupName: String,
    val syncGroupId: String,
    val isLocal: Boolean,
    val isDefault: Boolean = false,
    val ip: String? = null,
) {
    fun toServer() = Server(
        ip = ip!!,
        syncGroupName = syncGroupName,
        syncGroupId = syncGroupId,
    )
}
