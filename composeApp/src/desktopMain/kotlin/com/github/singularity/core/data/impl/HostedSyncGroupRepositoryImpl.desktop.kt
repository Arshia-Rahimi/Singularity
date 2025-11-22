package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsLocalDataSource: `HostedSyncGroupsLocalDataSource.desktop`,
) : HostedSyncGroupRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncGroups = hostedSyncGroupsLocalDataSource.hostedSyncGroups
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

    override val defaultSyncGroup = syncGroups.map { it.firstOrNull { group -> group.isDefault } }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

    override suspend fun insert(group: HostedSyncGroup) {
        hostedSyncGroupsLocalDataSource.insert(group)
    }

    override suspend fun upsert(node: HostedSyncGroupNode) {
        hostedSyncGroupsLocalDataSource.upsert(node)
    }

    override suspend fun editName(groupName: String, group: HostedSyncGroup) {
        hostedSyncGroupsLocalDataSource.updateName(groupName, group.hostedSyncGroupId)
    }

    override suspend fun delete(group: HostedSyncGroup) {
        hostedSyncGroupsLocalDataSource.delete(group)
    }

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupsLocalDataSource.setAsDefault(group)
    }

    override suspend fun removeAllDefaults() {
        hostedSyncGroupsLocalDataSource.removeAllDefaults()
    }

}
