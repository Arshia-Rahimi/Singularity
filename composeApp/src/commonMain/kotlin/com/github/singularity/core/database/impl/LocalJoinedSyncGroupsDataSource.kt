package com.github.singularity.core.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.JoinedSyncGroupDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.database.toBoolean
import com.github.singularity.core.database.toLong
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalJoinedSyncGroupsDataSource(
    private val db: SingularityDatabase,
) : JoinedSyncGroupDataSource {

    override val joinedSyncGroups = db.joinedSyncGroupsQueries.index()
        .asFlow()
        .map { query ->
            query.executeAsList().map {
                JoinedSyncGroup(
                    isDefault = it.is_default.toBoolean(),
                    authToken = it.auth_token,
                    syncGroupId = it.joined_sync_group_id,
                    syncGroupName = it.name,
                    ip = it.ip,
                )
            }
        }

    override fun insert(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.insert(
            joined_sync_group_id = joinedSyncGroup.syncGroupId,
            name = joinedSyncGroup.syncGroupName,
            auth_token = joinedSyncGroup.authToken,
            ip = joinedSyncGroup.ip,
        )
    }

    override fun delete(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.delete(joinedSyncGroup.syncGroupId)
    }

    override suspend fun setAsDefault(joinedSyncGroup: JoinedSyncGroup) {
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
