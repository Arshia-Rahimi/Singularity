package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.database.HostedSyncGroupDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsDataSource: HostedSyncGroupDataSource,
) : HostedSyncGroupRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val syncGroups = hostedSyncGroupsDataSource.hostedSyncGroups
        .shareInWhileSubscribed(1, scope)

    override suspend fun create(group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.insert(group)
    }

    override suspend fun create(node: HostedSyncGroupNode) {
        hostedSyncGroupsDataSource.insert(node)
    }

    override suspend fun editName(groupName: String, group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.updateName(groupName, group.hostedSyncGroupId)
    }

    override suspend fun delete(group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.delete(group)
    }

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.setAsDefault(group)
    }

}
