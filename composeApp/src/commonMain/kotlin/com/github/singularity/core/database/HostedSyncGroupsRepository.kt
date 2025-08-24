package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.database.entities.HostedSyncGroupNode
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class HostedSyncGroupsRepository(
    private val db: SingularityDatabase,
) {

    val hostedSyncGroups = db.hostedSyncGroupsQueries.index()
        .asFlow()
        .map { query ->
            query.executeAsList()
                .groupBy { it.hostedSyncGroupId }
                .map { (id, nodes) ->
                    val groupData = nodes.first()
                    HostedSyncGroup(
                        id = id,
                        name = groupData.name,
                        isDefault = groupData.isDefault == 1L,
                        nodes = nodes.map { node ->
                            HostedSyncGroupNode(
                                nodeId = node.nodeId,
                                authToken = node.authToken,
                                syncGroupId = id,
                            )
                        }
                    )
                }
        }.distinctUntilChanged()

    suspend fun insert(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.insert(
            hostedSyncGroupId = syncGroup.id,
            name = syncGroup.name,
            isDefault = syncGroup.isDefault.toLong(),
        )
    }

    suspend fun insert(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.insert(
            nodeId = syncGroupNode.nodeId,
            authToken = syncGroupNode.authToken,
            hostedSyncGroupId = syncGroupNode.syncGroupId,
        )
    }

    suspend fun delete(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.delete(syncGroupNode.nodeId)
    }

    suspend fun update(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.update(
            name = syncGroup.name,
            isDefault = syncGroup.isDefault.toLong(),
            hostedSyncGroupId = syncGroup.id,
        )
    }

    suspend fun delete(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.delete(syncGroup.id)
    }

    private fun Boolean.toLong() = if (this) 1L else 0L

}
