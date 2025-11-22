package com.github.singularity.core.datasource

import com.github.singularity.core.shared.model.JoinedSyncGroup
import kotlinx.coroutines.flow.Flow

interface JoinedSyncGroupsLocalDataSource {

    val joinedSyncGroups: Flow<List<JoinedSyncGroup>>

    fun upsert(joinedSyncGroup: JoinedSyncGroup)

    fun delete(joinedSyncGroup: JoinedSyncGroup)

    suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup)

    suspend fun removeAllDefaults()

}
