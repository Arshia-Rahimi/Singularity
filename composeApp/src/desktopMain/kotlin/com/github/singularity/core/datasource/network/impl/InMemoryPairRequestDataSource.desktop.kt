package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.datasource.network.PairCheckModel
import com.github.singularity.core.datasource.network.PairRequestDataSource
import com.github.singularity.core.datasource.network.PairRequestDto
import com.github.singularity.core.datasource.network.PairStatus
import com.github.singularity.core.shared.util.replaceFirstWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryPairRequestDataSource : PairRequestDataSource {

	private val _requests = MutableStateFlow<List<PairCheckModel>>(emptyList())
    override val requests = _requests.asStateFlow()

	override fun add(id: Int, pairRequest: PairRequestDto) {
		_requests.value += PairCheckModel(
            requestId = id,
            node = pairRequest.toNode(),
        )
    }

    override fun remove(id: Int) {
        _requests.value = _requests.value.filter { it.requestId == id }
    }

	override fun approve(nodeId: String) {
        _requests.value = _requests.value.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Approved) },
	        predicate = { nodeId == it.node.deviceId }
        )
    }

	override fun reject(nodeId: String) {
        _requests.value = _requests.value.replaceFirstWith(
            newItem = { it.copy(status = PairStatus.Rejected) },
	        predicate = { nodeId == it.node.deviceId }
        )
    }

    override fun get(id: Int) = requests.value.firstOrNull { it.requestId == id }

	override fun get(nodeId: String) = requests.value.firstOrNull { it.node.deviceId == nodeId }

	override fun clear() {
        _requests.value = emptyList()
    }

}