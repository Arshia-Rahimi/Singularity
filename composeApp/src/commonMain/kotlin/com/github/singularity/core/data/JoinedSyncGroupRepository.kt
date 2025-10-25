package com.github.singularity.core.data

import com.github.singularity.core.shared.model.JoinedSyncGroup
import kotlinx.coroutines.flow.SharedFlow

interface JoinedSyncGroupRepository {

    val joinedSyncGroups: SharedFlow<List<JoinedSyncGroup>>

    val defaultJoinedSyncGroup: SharedFlow<JoinedSyncGroup?>

    suspend fun upsert(group: JoinedSyncGroup)

    suspend fun delete(group: JoinedSyncGroup)

    suspend fun setAsDefault(group: JoinedSyncGroup)

    suspend fun removeAllDefaults()

}
