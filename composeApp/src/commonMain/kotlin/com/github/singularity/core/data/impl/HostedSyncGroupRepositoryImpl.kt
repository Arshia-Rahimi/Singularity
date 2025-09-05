package com.github.singularity.core.data.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.database.HostedSyncGroupDataSource
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class HostedSyncGroupRepositoryImpl(
    private val hostedSyncGroupsDataSource: HostedSyncGroupDataSource,
    scope: CoroutineScope,
) : HostedSyncGroupRepository {

    override val syncGroups = hostedSyncGroupsDataSource.hostedSyncGroups
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override val defaultSyncGroup = syncGroups.map { it.firstOrNull { group -> group.isDefault } }

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.insert(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun editName(groupName: String, group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.updateName(groupName, group.hostedSyncGroupId)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun delete(group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.delete(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.setAsDefault(group)
    }

}
