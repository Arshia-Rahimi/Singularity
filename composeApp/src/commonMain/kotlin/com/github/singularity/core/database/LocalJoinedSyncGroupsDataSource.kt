package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.entities.JoinedSyncGroup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalJoinedSyncGroupsDataSource(
    private val db: SingularityDatabase,
) {

    val joinedSyncGroups = db.joinedSyncGroupsQueries.index()
        .asFlow()
        .map { query ->
            query.executeAsList().map {
                JoinedSyncGroup(
                    joinedSyncGroupId = it.joined_sync_group_id,
                    name = it.name,
                    isDefault = it.is_default.toBoolean(),
                    authToken = it.auth_token,
                )
            }
        }

    fun insert(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.insert(
            joined_sync_group_id = joinedSyncGroup.joinedSyncGroupId,
            name = joinedSyncGroup.name,
            auth_token = joinedSyncGroup.authToken,
        )
    }

    fun delete(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.delete(joinedSyncGroup.joinedSyncGroupId)
    }

    suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup) {
        val groups = joinedSyncGroups.first()
        db.joinedSyncGroupsQueries.transaction {
            groups.forEach { group ->
                val isDefault = group.joinedSyncGroupId == joinedSyncGroup.joinedSyncGroupId
                db.joinedSyncGroupsQueries.updateIsDefault(
                    joined_sync_group_id = group.joinedSyncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

}
