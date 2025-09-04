package com.github.singularity.core.database.entities

data class JoinedSyncGroup(
    val authToken: String,
    val syncGroupName: String,
    val syncGroupId: String,
    val isDefault: Boolean = false,
    val ip: String? = null,
)
