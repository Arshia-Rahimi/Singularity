package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BroadcastRepository {

    val isBroadcasting: StateFlow<Boolean>

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    val pairRequests: StateFlow<List<Node>>

    fun create(group: HostedSyncGroup): Flow<Resource<Success>>

    fun editName(groupName: String, group: HostedSyncGroup): Flow<Resource<Success>>

    fun delete(group: HostedSyncGroup): Flow<Resource<Success>>

    suspend fun setAsDefault(group: HostedSyncGroup)

    fun approvePairRequest(node: Node)

    fun rejectPairRequest(node: Node)

    suspend fun startBroadcast()

    fun stopBroadcast()
    
}
