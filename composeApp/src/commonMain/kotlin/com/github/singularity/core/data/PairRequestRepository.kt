package com.github.singularity.core.data

import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.PairCheck
import com.github.singularity.core.shared.model.http.PairRequest
import kotlinx.coroutines.flow.StateFlow

interface PairRequestRepository {

    val requests: StateFlow<List<PairCheck>>

    fun add(id: Long, pairRequest: PairRequest)

    fun remove(id: Long)

    fun approve(node: Node)

    fun reject(node: Node)

    fun get(id: Long): PairCheck?

}
