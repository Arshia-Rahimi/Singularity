package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class BroadcastRepositoryImpl(
	private val hostedSyncGroupRepo: HostedSyncGroupRepository,
	private val pairRequestRepo: PairRequestDataSource,
) : BroadcastRepository {

    override val syncGroups = hostedSyncGroupRepo.syncGroups

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.insert(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun editName(groupName: String, group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.editName(groupName, group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun delete(group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.delete(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupRepo.setAsDefault(group)
    }

    override suspend fun removeAllDefaults() {
        hostedSyncGroupRepo.removeAllDefaults()
    }

    override fun approvePairRequest(node: Node) {
        pairRequestRepo.approve(node)
    }

    override fun rejectPairRequest(node: Node) {
        pairRequestRepo.reject(node)
    }

}
