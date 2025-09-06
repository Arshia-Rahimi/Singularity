package com.github.singularity.core.data

import com.github.singularity.core.database.HostedSyncGroupNodes
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ServerConnectionRepository {

    val syncEvents: SharedFlow<SyncEvent>

    val nodesJoined: SharedFlow<HostedSyncGroupNodes>

    val nodesConnected: SharedFlow<HostedSyncGroupNodes>

    val requestedNodes: Flow<Node>

    fun approve(node: Node)

    suspend fun send(event: SyncEvent)

}