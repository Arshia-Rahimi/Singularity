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
                    joinedSyncGroupId = it.joinedSyncGroupId,
                    name = it.name,
                    isDefault = it.isDefault.toBoolean(),
                    authToken = it.authToken,
                )
            }
        }

    fun insert(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.insert(
            joinedSyncGroupId = joinedSyncGroup.joinedSyncGroupId,
            name = joinedSyncGroup.name,
            authToken = joinedSyncGroup.authToken,
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
                    joinedSyncGroupId = group.joinedSyncGroupId,
                    isDefault = isDefault.toLong(),
                )
            }
        }
    }

}
