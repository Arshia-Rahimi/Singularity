package com.github.singularity.core.data

import com.github.singularity.core.common.util.Resource
import com.github.singularity.core.common.util.Success
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.mdns.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface BroadcastRepository {

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    fun create(group: HostedSyncGroup): Flow<Resource<Success>>

    fun delete(group: HostedSyncGroup): Flow<Resource<Success>>

    fun broadcastGroup(group: HostedSyncGroup): Flow<Node>

    fun approvePairRequest(node: Node): Flow<Resource<Success>>

    fun stopBroadcast()
    
}
