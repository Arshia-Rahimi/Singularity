package com.github.singularity.core.database

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.flow.Flow

interface HostedSyncGroupsDataSource {

    val hostedSyncGroups: Flow<List<HostedSyncGroup>>

    fun insert(syncGroup: HostedSyncGroup)

    fun updateName(groupName: String, groupId: String)

    fun insert(syncGroupNode: HostedSyncGroupNode)

    fun delete(syncGroupNode: HostedSyncGroupNode)

    fun delete(syncGroup: HostedSyncGroup)

    suspend fun setAsDefault(hostedSyncGroup: HostedSyncGroup)

}
