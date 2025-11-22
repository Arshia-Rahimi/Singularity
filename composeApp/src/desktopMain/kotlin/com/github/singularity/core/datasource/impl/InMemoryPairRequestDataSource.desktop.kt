package com.github.singularity.core.datasource.impl

import com.github.singularity.core.datasource.PairRequestDataSource
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.PairCheck
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.util.replaceFirstWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryPairRequestDataSource : PairRequestDataSource {

    private val _requests = MutableStateFlow<List<PairCheck>>(emptyList())
    override val requests = _requests.asStateFlow()

    override fun add(id: Int, pairRequest: PairRequest) {
        _requests.value += PairCheck(
            requestId = id,
            node = pairRequest.toNode(),
        )
    }

    override fun remove(id: Int) {
        _requests.value = _requests.value.filter { it.requestId == id }
    }

    override fun approve(node: Node) {
        _requests.value = _requests.value.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Approved) },
            predicate = { node.deviceId == it.node.deviceId }
        )
    }

    override fun reject(node: Node) {
        _requests.value = _requests.value.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Rejected) },
            predicate = { node == it.node }
        )
    }

    override fun get(id: Int) = requests.value.firstOrNull { it.requestId == id }

    override fun clear() {
        _requests.value = emptyList()
    }

}