package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.network.NodeModel
import kotlinx.coroutines.flow.SharedFlow

interface BroadcastRepository {

	val syncGroups: SharedFlow<List<HostedSyncGroupModel>>

	suspend fun create(group: HostedSyncGroupModel)

	suspend fun editName(groupName: String, groupId: String)

	suspend fun delete(groupId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

	fun approvePairRequest(node: NodeModel)

	fun rejectPairRequest(node: NodeModel)

}
