package com.github.singularity.core.database

import com.github.singularity.core.shared.model.JoinedSyncGroup
import kotlinx.coroutines.flow.Flow

interface JoinedSyncGroupsDataSource {

    val joinedSyncGroups: Flow<List<JoinedSyncGroup>>

    fun insert(joinedSyncGroup: JoinedSyncGroup)

    fun delete(joinedSyncGroup: JoinedSyncGroup)

    suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup)

}
