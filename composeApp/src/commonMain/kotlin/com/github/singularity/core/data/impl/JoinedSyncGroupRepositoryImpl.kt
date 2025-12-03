package com.github.singularity.core.data.impl

import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.database.JoinedSyncGroupsLocalDataSource
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

	override suspend fun upsert(group: JoinedSyncGroupModel) {
        joinedSyncGroupsLocalDataSource.upsert(group)
    }

	override suspend fun delete(groupId: String) {
		joinedSyncGroupsLocalDataSource.delete(groupId)
    }

	override suspend fun setAsDefault(groupId: String) {
		joinedSyncGroupsLocalDataSource.setAsDefault(groupId)
    }

    override suspend fun removeAllDefaults() {
        joinedSyncGroupsLocalDataSource.removeAllDefaults()
    }

}
