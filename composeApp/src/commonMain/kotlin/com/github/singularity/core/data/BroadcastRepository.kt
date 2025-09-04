package com.github.singularity.core.data

import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.Success
import com.github.singularity.data.entities.HostedSyncGroup
import com.github.singularity.models.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface BroadcastRepository {

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    fun create(group: HostedSyncGroup): Flow<Resource<Success>>

    fun editName(groupName: String, group: HostedSyncGroup): Flow<Resource<Success>>

    fun delete(group: HostedSyncGroup): Flow<Resource<Success>>

    fun broadcastGroup(group: HostedSyncGroup): Flow<Node>

    suspend fun setAsDefault(group: HostedSyncGroup)

    fun approvePairRequest(node: Node): Flow<Resource<Success>>

    fun stopBroadcast()
    
}
