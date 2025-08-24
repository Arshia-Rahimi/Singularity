package com.github.singularity.core.database.entities

data class JoinedSyncGroup(
    val joinedSyncGroupId: String,
    val name: String,
    val isDefault: Boolean,
    val authToken: String,
)
