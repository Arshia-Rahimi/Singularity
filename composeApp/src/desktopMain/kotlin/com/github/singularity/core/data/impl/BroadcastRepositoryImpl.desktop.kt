package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.network.NodeModel
import com.github.singularity.core.datasource.network.PairRequestDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BroadcastRepositoryImpl(
	private val hostedSyncGroupRepo: HostedSyncGroupRepository,
	private val pairRequestRepo: PairRequestDataSource,
) : BroadcastRepository {

	override val syncGroups = hostedSyncGroupRepo.syncGroups

	override suspend fun create(group: HostedSyncGroupModel) =
		hostedSyncGroupRepo.insert(group)

	override suspend fun editName(groupName: String, groupId: String) =
		hostedSyncGroupRepo.editName(groupName, groupId)

	override suspend fun delete(groupId: String) =
		hostedSyncGroupRepo.delete(groupId)

	override suspend fun setAsDefault(groupId: String) {
		hostedSyncGroupRepo.setAsDefault(groupId)
	}

	override suspend fun removeAllDefaults() {
		hostedSyncGroupRepo.removeAllDefaults()
	}

	override fun approvePairRequest(node: NodeModel) =
		pairRequestRepo.approve(node)

	override fun rejectPairRequest(node: NodeModel) =
		pairRequestRepo.reject(node)

}
