package com.github.singularity.core.shared.model.http

import kotlinx.serialization.Serializable

@Serializable
data class PairResponse(
    val success: Boolean,
    val authToken: String? = null,
    val message: String? = null,
)
