package com.github.singularity.core.shared.model

data class JoinedSyncGroup(
    val authToken: String,
    val syncGroupName: String,
    val syncGroupId: String,
    val isDefault: Boolean = false,
)
