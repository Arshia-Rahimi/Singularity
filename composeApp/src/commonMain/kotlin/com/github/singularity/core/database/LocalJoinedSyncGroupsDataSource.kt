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
                    isDefault = it.is_default.toBoolean(),
                    authToken = it.auth_token,
                    isLocal = it.is_local.toBoolean(),
                    syncGroupId = it.joined_sync_group_id,
                    syncGroupName = it.name,
                    ip = it.ip,
                )
            }
        }

    fun insert(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.insert(
            joined_sync_group_id = joinedSyncGroup.syncGroupId,
            name = joinedSyncGroup.syncGroupName,
            auth_token = joinedSyncGroup.authToken,
            is_local = joinedSyncGroup.isLocal.toLong(),
            ip = joinedSyncGroup.ip,
        )
    }

    fun delete(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.delete(joinedSyncGroup.syncGroupId)
    }

    suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup) {
        val groups = joinedSyncGroups.first()
        db.joinedSyncGroupsQueries.transaction {
            groups.forEach { group ->
                val isDefault = group.syncGroupId == joinedSyncGroup.syncGroupId
                db.joinedSyncGroupsQueries.updateIsDefault(
                    joined_sync_group_id = group.syncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

}
