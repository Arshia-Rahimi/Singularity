package com.github.singularity.core.data.impl

import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.PairCheck
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.util.replaceFirstWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PairRequestRepositoryImpl : PairRequestRepository {

    private val _requests = MutableStateFlow<List<PairCheck>>(emptyList())
    override val requests = _requests.asStateFlow()

    override fun add(id: Long, pairRequest: PairRequest) {
        _requests.value = _requests.value + PairCheck(
            requestId = id,
            node = pairRequest.toNode(),
        )
    }

    override fun remove(id: Long) {
        val list = _requests.value.toMutableList()
        list.removeAll { it.requestId == id }
        _requests.value = list
    }

    override fun approve(node: Node) {
        val list = _requests.value.toMutableList()
        list.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Approved) },
            predicate = { node == it.node }
        )
        _requests.value = list
    }

    override fun reject(node: Node) {
        val list = _requests.value.toMutableList()
        list.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Approved) },
            predicate = { node == it.node }
        )
        _requests.value = list
    }

    override fun get(id: Long) = requests.value.firstOrNull { it.requestId == id }

}
