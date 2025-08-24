package com.github.singularity.core.database.entities

data class HostedSyncGroupEntity(
    val id: String,
    val name: String,
    val default: Boolean,
)
