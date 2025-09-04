package com.github.singularity.data

import com.github.singularity.data.entities.HostedSyncGroup
import com.github.singularity.data.entities.HostedSyncGroupNode
import kotlinx.coroutines.flow.Flow

interface HostedSyncGroupDataSource {

    val hostedSyncGroups: Flow<List<HostedSyncGroup>>

    fun insert(syncGroup: HostedSyncGroup)

    fun updateName(groupName: String, groupId: String)

    fun insert(syncGroupNode: HostedSyncGroupNode)

    fun delete(syncGroupNode: HostedSyncGroupNode)

    fun delete(syncGroup: HostedSyncGroup)

    suspend fun setAsDefault(hostedSyncGroup: HostedSyncGroup)

}
