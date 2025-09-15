package com.github.singularity.core.data

import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.PairCheck
import com.github.singularity.core.shared.model.http.PairRequest
import kotlinx.coroutines.flow.StateFlow

interface PairRequestRepository {

    val requests: StateFlow<List<PairCheck>>

    fun add(id: Int, pairRequest: PairRequest)

    fun remove(id: Int)

    fun approve(node: Node)

    fun reject(node: Node)

    fun get(id: Int): PairCheck?

}
