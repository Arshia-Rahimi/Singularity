package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.coroutines.asFlow
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SqlDelightHostedSyncGroupsLocalDataSource(
    db: SingularityDatabase,
) : HostedSyncGroupsLocalDataSource {

    private val queries = db.hostedSyncGroupsQueries
    private val nodesQueries = db.hostedSyncGroupNodesQueries

    override val hostedSyncGroups = queries.index()
        .asFlow()
        .map { query ->
            query.executeAsList()
                .groupBy { it.id }
                .map { (id, nodes) ->
                    val groupData = nodes.first()
                    HostedSyncGroup(
                        hostedSyncGroupId = id,
                        name = groupData.name,
                        isDefault = groupData.is_default.toBoolean(),
                        nodes = nodes.mapNotNull { node ->
                            if (node.hosted_sync_group_id == null) return@mapNotNull null
                            HostedSyncGroupNode(
                                deviceId = node.id,
                                authToken = node.auth_token ?: "",
                                syncGroupId = id,
                                syncGroupName = groupData.name,
                                deviceName = node.name,
                                deviceOs = node.node_os ?: "",
                            )
                        },
                    )
                }
        }.distinctUntilChanged()

    override fun insert(syncGroup: HostedSyncGroup) {
        queries.insert(
            id = syncGroup.hostedSyncGroupId,
            name = syncGroup.name,
        )
    }

    override fun updateName(groupName: String, groupId: String) {
        queries.updateName(groupName, groupId)
    }

    override fun upsert(syncGroupNode: HostedSyncGroupNode) {
        if (
            nodesQueries.update(
                auth_token = syncGroupNode.authToken,
                hosted_sync_group_id = syncGroupNode.syncGroupId,
                node_name = syncGroupNode.deviceName,
                node_os = syncGroupNode.deviceOs,
                id = syncGroupNode.deviceId,
            ).value == 0L
        ) {
            nodesQueries.insert(
                auth_token = syncGroupNode.authToken,
                hosted_sync_group_id = syncGroupNode.syncGroupId,
                node_name = syncGroupNode.deviceName,
                node_os = syncGroupNode.deviceOs,
                id = syncGroupNode.deviceId,
            )
        }
    }

    override fun delete(syncGroupNode: HostedSyncGroupNode) {
        nodesQueries.delete(syncGroupNode.deviceId)
    }

    override fun delete(syncGroup: HostedSyncGroup) {
        queries.delete(syncGroup.hostedSyncGroupId)
    }

    override suspend fun setAsDefault(hostedSyncGroup: HostedSyncGroup) {
        val groups = hostedSyncGroups.first()
        queries.transaction {
            groups.forEach { group ->
                val isDefault = group.hostedSyncGroupId == hostedSyncGroup.hostedSyncGroupId
                queries.updateIsDefault(
                    id = group.hostedSyncGroupId,
                    is_default = isDefault.toLong(),
                )
            }
        }
    }

    override suspend fun removeAllDefaults() {
        val groups = hostedSyncGroups.first()
        queries.transaction {
            groups.forEach { group ->
                queries.updateIsDefault(
                    is_default = false.toLong(),
                    id = group.hostedSyncGroupId,
                )
            }
        }
    }

}