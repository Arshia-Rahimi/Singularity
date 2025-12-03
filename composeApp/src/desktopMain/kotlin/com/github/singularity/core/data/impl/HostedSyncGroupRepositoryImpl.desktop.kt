package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import com.github.singularity.core.datasource.database.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HostedSyncGroupRepositoryImpl(
	private val hostedSyncGroupsLocalDataSource: HostedSyncGroupsLocalDataSource,
) : HostedSyncGroupRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncGroups = hostedSyncGroupsLocalDataSource.hostedSyncGroups
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

    override val defaultSyncGroup = syncGroups.map { it.firstOrNull { group -> group.isDefault } }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

	override suspend fun insert(group: HostedSyncGroupModel) {
        hostedSyncGroupsLocalDataSource.insert(group)
    }

	override suspend fun upsert(node: HostedSyncGroupNodeModel) {
        hostedSyncGroupsLocalDataSource.upsert(node)
    }

	override suspend fun editName(groupName: String, groupId: String) {
		hostedSyncGroupsLocalDataSource.updateName(groupName, groupId)
    }

	override suspend fun delete(groupId: String) {
		hostedSyncGroupsLocalDataSource.delete(groupId)
    }

	override suspend fun setAsDefault(groupId: String) {
		hostedSyncGroupsLocalDataSource.setAsDefault(groupId)
    }

    override suspend fun removeAllDefaults() {
        hostedSyncGroupsLocalDataSource.removeAllDefaults()
    }

}
