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
                .groupBy { it.hosted_sync_group_id }
                .map { (id, nodes) ->
                    val groupData = nodes.first()
                    HostedSyncGroup(
                        hostedSyncGroupId = id,
                        name = groupData.name,
                        isDefault = groupData.is_default.toBoolean(),
                        nodes = nodes.mapNotNull { node ->
                            if (node.node_id == null) return@mapNotNull null
                            HostedSyncGroupNode(
                                nodeId = node.node_id,
                                authToken = node.auth_token ?: "",
                                syncGroupId = id,
                            )
                        },
                    )
                }
        }.distinctUntilChanged()

    fun insert(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.insert(
            hosted_sync_group_id = syncGroup.hostedSyncGroupId,
            name = syncGroup.name,
        )
    }

    fun updateName(groupName: String, groupId: String) {
        db.hostedSyncGroupsQueries.updateName(groupName, groupId)
    }

    fun insert(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.insert(
            node_id = syncGroupNode.nodeId,
            auth_token = syncGroupNode.authToken,
            hosted_sync_group_id = syncGroupNode.syncGroupId,
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
                    hosted_sync_group_id = group.hostedSyncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

}
