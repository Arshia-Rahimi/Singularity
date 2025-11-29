package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import kotlinx.coroutines.flow.SharedFlow

interface BroadcastRepository {

    val syncGroups: SharedFlow<List<HostedSyncGroup>>

    suspend fun create(group: HostedSyncGroup)

    suspend fun editName(groupName: String, group: HostedSyncGroup)

    suspend fun delete(group: HostedSyncGroup)

    suspend fun setAsDefault(group: HostedSyncGroup)

    suspend fun removeAllDefaults()

    fun approvePairRequest(node: Node)

    fun rejectPairRequest(node: Node)

}
