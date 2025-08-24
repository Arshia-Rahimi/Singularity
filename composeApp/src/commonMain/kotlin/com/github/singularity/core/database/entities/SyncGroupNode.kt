package com.github.singularity.core.database.entities

data class SyncGroupNode(
    val deviceId: String,
    val authToken: String,
    val syncGroupId: String,
)
