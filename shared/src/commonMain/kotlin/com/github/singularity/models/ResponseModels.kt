package com.github.singularity.models

import kotlinx.serialization.Serializable

@Serializable
data class PairRequestResponse(
    val success: Boolean,
    val authToken: String? = null,
    val message: String? = null,
)
