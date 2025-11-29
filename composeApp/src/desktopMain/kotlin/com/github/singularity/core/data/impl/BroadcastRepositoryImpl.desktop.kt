package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BroadcastRepositoryImpl(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val pairRequestRepo: PairRequestDataSource,
) : BroadcastRepository {

    override val syncGroups = hostedSyncGroupRepo.syncGroups

    override suspend fun create(group: HostedSyncGroup) =
        hostedSyncGroupRepo.insert(group)

    override suspend fun editName(groupName: String, group: HostedSyncGroup) =
        hostedSyncGroupRepo.editName(groupName, group)

    override suspend fun delete(group: HostedSyncGroup) =
        hostedSyncGroupRepo.delete(group)

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
