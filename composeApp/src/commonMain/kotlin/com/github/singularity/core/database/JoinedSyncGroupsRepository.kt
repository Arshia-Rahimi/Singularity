package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.entities.JoinedSyncGroup
import kotlinx.coroutines.flow.map

class JoinedSyncGroupsRepository(
    private val db: SingularityDatabase,
) {

    val joinedSyncGroup = db.joinedSyncGroupsQueries.index()
        .asFlow()
        .map { query ->
            query.executeAsList().map {
                JoinedSyncGroup(
                    joinedSyncGroupId = it.joinedSyncGroupId,
                    name = it.name,
                    isDefault = it.isDefault == 1L,
                    authToken = it.authToken,
                )
            }
        }

    suspend fun insert(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.insert(
            joinedSyncGroupId = joinedSyncGroup.joinedSyncGroupId,
            isDefault = joinedSyncGroup.isDefault.toLong(),
            name = joinedSyncGroup.name,
            authToken = joinedSyncGroup.authToken,
        )
    }

    suspend fun update(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.update(
            isDefault = joinedSyncGroup.isDefault.toLong(),
            name = joinedSyncGroup.name,
            authToken = joinedSyncGroup.authToken,
            joinedSyncGroupId = joinedSyncGroup.joinedSyncGroupId,
        )
    }

    suspend fun delete(joinedSyncGroup: JoinedSyncGroup) {
        db.joinedSyncGroupsQueries.delete(joinedSyncGroup.joinedSyncGroupId)
    }

    private fun Boolean.toLong() = if (this) 1L else 0L

}
