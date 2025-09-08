package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.database.HostedSyncGroupDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsDataSource: HostedSyncGroupDataSource,
    scope: CoroutineScope,
) : HostedSyncGroupRepository {

    override val syncGroups = hostedSyncGroupsDataSource.hostedSyncGroups
        .shareInWhileSubscribed(scope)

    override val defaultGroup = syncGroups.map {
        it.firstOrNull { group -> group.isDefault }
    }
        .distinctUntilChanged()
        .shareInWhileSubscribed(scope)

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
