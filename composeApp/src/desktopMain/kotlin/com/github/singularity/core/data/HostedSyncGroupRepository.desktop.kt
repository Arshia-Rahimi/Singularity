package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.flow.SharedFlow

interface HostedSyncGroupRepository {

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    val defaultSyncGroup: SharedFlow<HostedSyncGroup?>

    suspend fun insert(group: HostedSyncGroup)

    suspend fun upsert(node: HostedSyncGroupNode)

    suspend fun editName(groupName: String, group: HostedSyncGroup)

    suspend fun delete(group: HostedSyncGroup)

    suspend fun setAsDefault(group: HostedSyncGroup)

    suspend fun removeAllDefaults()

}
