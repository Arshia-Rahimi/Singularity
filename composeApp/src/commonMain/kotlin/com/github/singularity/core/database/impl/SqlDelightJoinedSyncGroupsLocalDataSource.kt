package com.github.singularity.core.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.database.toBoolean
import com.github.singularity.core.database.toLong
import com.github.singularity.core.shared.model.JoinedSyncGroup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SqlDelightJoinedSyncGroupsLocalDataSource(
    db: SingularityDatabase,
) : JoinedSyncGroupsLocalDataSource {

    private val queries = db.joinedSyncGroupsQueries

    override val joinedSyncGroups = queries.index()
        .asFlow()
        .map { query ->
            query.executeAsList().map {
                JoinedSyncGroup(
                    isDefault = it.is_default.toBoolean(),
                    authToken = it.auth_token,
                    syncGroupId = it.joined_sync_group_id,
                    syncGroupName = it.name,
                )
            }
        }

    override fun insert(joinedSyncGroup: JoinedSyncGroup) {
        queries.insert(
            joined_sync_group_id = joinedSyncGroup.syncGroupId,
            name = joinedSyncGroup.syncGroupName,
            auth_token = joinedSyncGroup.authToken,
        )
    }

    override fun delete(joinedSyncGroup: JoinedSyncGroup) {
        queries.delete(joinedSyncGroup.syncGroupId)
    }

    override suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup) {
        val groups = joinedSyncGroups.first()
        queries.transaction {
            groups.forEach { group ->
                val isDefault = group.syncGroupId == joinedSyncGroup.syncGroupId
                queries.updateIsDefault(
                    joined_sync_group_id = group.syncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

}
