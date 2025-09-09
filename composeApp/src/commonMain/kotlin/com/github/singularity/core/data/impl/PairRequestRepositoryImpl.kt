package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PairRequestRepositoryImpl : PairRequestRepository {

    private val _requests = MutableStateFlow<Map<Long, PairStatus>>(emptyMap())
    override val requests = _requests.asStateFlow()

    override fun add(id: Long, pairRequest: PairRequest) {
        _requests.value = _requests.value + (id to PairStatus.Awaiting)
    }

    override fun remove(id: Long) {
        _requests.value = _requests.value - id
    }

    override fun approve(id: Long) {
        _requests.value = _requests.value.toMutableMap().apply { set(id, PairStatus.Approved) }
    }

    override fun reject(id: Long) {
        _requests.value = _requests.value.toMutableMap().apply { set(id, PairStatus.Rejected) }
    }

    override fun getStatus(id: Long) = requests.value[id] ?: PairStatus.Error

}
