package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.shared.model.JoinedSyncGroup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SqliteJoinedSyncGroupsDataSource(
    db: SingularityDatabase,
) {

    private val queries = db.joinedSyncGroupsQueries

    val joinedSyncGroups = queries.index()
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

    fun insert(joinedSyncGroup: JoinedSyncGroup) {
        queries.insert(
            joined_sync_group_id = joinedSyncGroup.syncGroupId,
            name = joinedSyncGroup.syncGroupName,
            auth_token = joinedSyncGroup.authToken,
        )
    }

    fun delete(joinedSyncGroup: JoinedSyncGroup) {
        queries.delete(joinedSyncGroup.syncGroupId)
    }

    suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup) {
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
