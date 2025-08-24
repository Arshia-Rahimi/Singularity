package com.github.singularity.core.database.entities

data class SyncGroupEntity(
    val id: String,
    val name: String,
    val default: Boolean,
    val authToken: String,
)
