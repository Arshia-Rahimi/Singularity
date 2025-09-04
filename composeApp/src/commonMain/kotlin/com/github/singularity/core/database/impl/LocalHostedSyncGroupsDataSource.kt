package com.github.singularity.core.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.HostedSyncGroupDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.database.entities.HostedSyncGroupNode
import com.github.singularity.core.database.toBoolean
import com.github.singularity.core.database.toLong
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalHostedSyncGroupsDataSource(
    private val db: SingularityDatabase,
) : HostedSyncGroupDataSource {

    override val hostedSyncGroups = db.hostedSyncGroupsQueries.index()
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

    override fun insert(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.insert(
            hosted_sync_group_id = syncGroup.hostedSyncGroupId,
            name = syncGroup.name,
        )
    }

    override fun updateName(groupName: String, groupId: String) {
        db.hostedSyncGroupsQueries.updateName(groupName, groupId)
    }

    override fun insert(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.insert(
            node_id = syncGroupNode.nodeId,
            auth_token = syncGroupNode.authToken,
            hosted_sync_group_id = syncGroupNode.syncGroupId,
        )
    }

    override fun delete(syncGroupNode: HostedSyncGroupNode) {
        db.hostedSyncGroupNodesQueries.delete(syncGroupNode.nodeId)
    }

    override fun delete(syncGroup: HostedSyncGroup) {
        db.hostedSyncGroupsQueries.delete(syncGroup.hostedSyncGroupId)
    }

    override suspend fun setAsDefault(hostedSyncGroup: HostedSyncGroup) {
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
