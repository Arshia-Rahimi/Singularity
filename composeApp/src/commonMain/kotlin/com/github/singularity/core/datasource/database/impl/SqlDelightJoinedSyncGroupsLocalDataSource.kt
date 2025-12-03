package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.database.JoinedSyncGroupsLocalDataSource
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
	            JoinedSyncGroupModel(
                    isDefault = it.is_default.toBoolean(),
                    authToken = it.auth_token,
                    syncGroupId = it.id,
                    syncGroupName = it.name,
                )
            }
        }

	override fun upsert(joinedSyncGroup: JoinedSyncGroupModel) {
        if (
            queries.update(
                id = joinedSyncGroup.syncGroupId,
                name = joinedSyncGroup.syncGroupName,
                auth_token = joinedSyncGroup.authToken,
            ).value == 0L
        ) {
            queries.insert(
                id = joinedSyncGroup.syncGroupId,
                name = joinedSyncGroup.syncGroupName,
                auth_token = joinedSyncGroup.authToken,
            )
        }
    }

	override fun delete(groupId: String) {
		queries.delete(groupId)
    }

	override suspend fun setAsDefault(groupId: String) {
        val groups = joinedSyncGroups.first()
        queries.transaction {
            groups.forEach { group ->
	            val isDefault = group.syncGroupId == groupId
                queries.updateIsDefault(
                    id = group.syncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

    override suspend fun removeAllDefaults() {
        val groups = joinedSyncGroups.first()
        queries.transaction {
            groups.forEach { group ->
                queries.updateIsDefault(
                    is_default = false.toLong(),
                    id = group.syncGroupId,
                )
            }
        }
    }

}
