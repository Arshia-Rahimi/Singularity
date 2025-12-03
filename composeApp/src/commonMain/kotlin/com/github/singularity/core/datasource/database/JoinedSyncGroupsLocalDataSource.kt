package com.github.singularity.core.datasource.database

import kotlinx.coroutines.flow.Flow

interface JoinedSyncGroupsLocalDataSource {

	val joinedSyncGroups: Flow<List<JoinedSyncGroupModel>>

	fun upsert(joinedSyncGroup: JoinedSyncGroupModel)

	fun delete(groupId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

}
