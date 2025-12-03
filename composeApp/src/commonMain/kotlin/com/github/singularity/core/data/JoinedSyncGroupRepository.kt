package com.github.singularity.core.data

import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import kotlinx.coroutines.flow.SharedFlow

interface JoinedSyncGroupRepository {

	val joinedSyncGroups: SharedFlow<List<JoinedSyncGroupModel>>

	val defaultJoinedSyncGroup: SharedFlow<JoinedSyncGroupModel?>

	suspend fun upsert(group: JoinedSyncGroupModel)

	suspend fun delete(groupId: String)

	suspend fun setAsDefault(groupId: String)

    suspend fun removeAllDefaults()

}
