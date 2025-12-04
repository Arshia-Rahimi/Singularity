package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import kotlinx.coroutines.flow.Flow

interface HostedSyncGroupRepository {

	val syncGroups: Flow<List<HostedSyncGroupModel>>

	val defaultSyncGroup: Flow<HostedSyncGroupModel?>

	suspend fun hasPairedBefore(deviceId: String, hostedSyncGroupId: String): Boolean

	suspend fun insert(group: HostedSyncGroupModel)

	suspend fun upsert(node: HostedSyncGroupNodeModel)

	suspend fun editName(groupName: String, groupId: String)

	suspend fun delete(groupId: String)

	suspend fun delete(groupId: String, nodeId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

}
