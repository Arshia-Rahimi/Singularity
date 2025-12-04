package com.github.singularity.core.datasource.database

import kotlinx.coroutines.flow.Flow

interface HostedSyncGroupsLocalDataSource {

	val hostedSyncGroups: Flow<List<HostedSyncGroupModel>>

	fun insert(syncGroup: HostedSyncGroupModel)

    fun updateName(groupName: String, groupId: String)

	fun upsert(syncGroupNode: HostedSyncGroupNodeModel)

	fun delete(groupId: String)

	fun delete(groupId: String, nodeId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

}
