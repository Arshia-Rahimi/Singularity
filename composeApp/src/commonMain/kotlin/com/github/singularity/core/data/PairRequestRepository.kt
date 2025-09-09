package com.github.singularity.core.data

import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairStatus
import kotlinx.coroutines.flow.StateFlow

interface PairRequestRepository {

    val requests: StateFlow<Map<Long, PairStatus>>

    fun add(id: Long, pairRequest: PairRequest)

    fun remove(id: Long)

    fun approve(id: Long)

    fun reject(id: Long)

    fun getStatus(id: Long): PairStatus

}
