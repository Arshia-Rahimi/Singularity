package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.flow.SharedFlow

interface HostedSyncGroupRepository {

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    val defaultGroup: SharedFlow<HostedSyncGroup?>

    suspend fun create(group: HostedSyncGroup)

    suspend fun create(node: HostedSyncGroupNode)

    suspend fun editName(groupName: String, group: HostedSyncGroup)

    suspend fun delete(group: HostedSyncGroup)

    suspend fun setAsDefault(group: HostedSyncGroup)

}
