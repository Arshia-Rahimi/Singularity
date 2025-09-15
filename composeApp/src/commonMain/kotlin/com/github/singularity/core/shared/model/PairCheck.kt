package com.github.singularity.core.shared.model

import com.github.singularity.core.shared.model.http.PairStatus

data class PairCheck(
    val requestId: Int,
    val node: Node,
    val status: PairStatus = PairStatus.Awaiting,
)
