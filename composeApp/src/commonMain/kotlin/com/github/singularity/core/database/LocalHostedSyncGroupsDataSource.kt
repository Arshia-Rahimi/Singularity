package com.github.singularity.core.database

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.database.entities.HostedSyncGroupNode
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalHostedSyncGroupsDataSource(
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
                        hostedSyncGroupId = id,
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

    fun insert(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.insert(
            hostedSyncGroupId = syncGroup.hostedSyncGroupId,
            name = syncGroup.name,
        )
    }

    fun insert(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.insert(
            nodeId = syncGroupNode.nodeId,
            authToken = syncGroupNode.authToken,
            hostedSyncGroupId = syncGroupNode.syncGroupId,
        )
    }

    fun delete(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.delete(syncGroupNode.nodeId)
    }

    fun delete(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.delete(syncGroup.hostedSyncGroupId)
    }

    suspend fun setAsDefault(hostedSyncGroup: HostedSyncGroup) {
        val groups = hostedSyncGroups.first()
        db.joinedSyncGroupsQueries.transaction {
            groups.forEach { group ->
                val isDefault = group.hostedSyncGroupId == hostedSyncGroup.hostedSyncGroupId
                db.hostedSyncGroupsQueries.updateIsDefault(
                    hostedSyncGroupId = group.hostedSyncGroupId,
                    isDefault = isDefault.toLong(),
                )
            }
        }
    }

    private fun Boolean.toLong() = if (this) 1L else 0L

}
