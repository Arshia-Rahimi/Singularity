package com.github.singularity.core.data

import com.github.singularity.core.database.HostedSyncGroupNodes
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ConnectionRepository {

    val syncMode: Flow<SyncMode>

    val syncEvents: SharedFlow<SyncEvent>

    val connectionState: SharedFlow<ConnectionState>

    val nodesJoined: SharedFlow<HostedSyncGroupNodes>

    val nodesConnected: SharedFlow<HostedSyncGroupNodes>

    val requestedNodes: Flow<Node>

    fun approve(node: Node)

    fun refresh()

    suspend fun send(event: SyncEvent)

    suspend fun toggleSyncMode()

}
