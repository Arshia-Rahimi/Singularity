package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.database.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsLocalDataSource: HostedSyncGroupsLocalDataSource,
) : HostedSyncGroupRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncGroups = hostedSyncGroupsLocalDataSource.hostedSyncGroups
        .shareInWhileSubscribed(scope, 1)

    override suspend fun create(group: HostedSyncGroup) {
        hostedSyncGroupsLocalDataSource.insert(group)
    }

    override suspend fun create(node: HostedSyncGroupNode) {
        hostedSyncGroupsLocalDataSource.insert(node)
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

}
