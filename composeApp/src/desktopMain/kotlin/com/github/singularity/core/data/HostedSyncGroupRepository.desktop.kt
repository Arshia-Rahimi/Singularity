package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import kotlinx.coroutines.flow.SharedFlow

interface HostedSyncGroupRepository {

	val syncGroups: SharedFlow<List<HostedSyncGroupModel>>

	val defaultSyncGroup: SharedFlow<HostedSyncGroupModel?>

	suspend fun insert(group: HostedSyncGroupModel)

	suspend fun upsert(node: HostedSyncGroupNodeModel)

	suspend fun editName(groupName: String, groupId: String)

	suspend fun delete(groupId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

}
