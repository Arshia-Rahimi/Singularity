package com.github.singularity.core.shared.model.http

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.serialization.Serializable

@Serializable
data class PairResponse(
    val success: Boolean,
    val pairRequestId: Long? = null,
)

@Serializable
data class PairCheckResponse(
    val pairStatus: PairStatus,
    val message: String? = null,
    val node: HostedSyncGroupNode? = null,
)

enum class PairStatus {
    Rejected, Awaiting, Approved, Error,
}
