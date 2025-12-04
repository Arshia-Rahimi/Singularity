package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import kotlinx.coroutines.flow.Flow

interface BroadcastRepository {

	val syncGroups: Flow<List<HostedSyncGroupModel>>

	suspend fun create(group: HostedSyncGroupModel)

	suspend fun editName(groupName: String, groupId: String)

	suspend fun delete(groupId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

	fun approvePairRequest(nodeId: String)

	fun rejectPairRequest(nodeId: String)

}
