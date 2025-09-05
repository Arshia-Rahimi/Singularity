package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.database.HostedSyncGroupDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsDataSource: HostedSyncGroupDataSource,
    scope: CoroutineScope,
) : HostedSyncGroupRepository {

    override val syncGroups = hostedSyncGroupsDataSource.hostedSyncGroups
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

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
