package com.github.singularity.core.data.impl

import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.datasource.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.util.shareInWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class JoinedSyncGroupRepositoryImpl(
    private val joinedSyncGroupsLocalDataSource: JoinedSyncGroupsLocalDataSource,
) : JoinedSyncGroupRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val joinedSyncGroups = joinedSyncGroupsLocalDataSource.joinedSyncGroups
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

    override val defaultJoinedSyncGroup = joinedSyncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .flowOn(Dispatchers.IO)
        .shareInWhileSubscribed(scope, 1)

    override suspend fun upsert(group: JoinedSyncGroup) {
        joinedSyncGroupsLocalDataSource.upsert(group)
    }

    override suspend fun delete(group: JoinedSyncGroup) {
        joinedSyncGroupsLocalDataSource.delete(group)
    }

    override suspend fun setAsDefault(group: JoinedSyncGroup) {
        joinedSyncGroupsLocalDataSource.setAsDefault(group)
    }

    override suspend fun removeAllDefaults() {
        joinedSyncGroupsLocalDataSource.removeAllDefaults()
    }

}
