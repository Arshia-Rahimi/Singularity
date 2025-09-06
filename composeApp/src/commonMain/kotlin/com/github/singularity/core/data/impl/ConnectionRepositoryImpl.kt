package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.ConnectionRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.HostedSyncGroupNodes
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ConnectionState
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import com.github.singularity.core.shared.util.next
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ConnectionRepositoryImpl(
    private val preferencesRepo: PreferencesRepository,
    private val clientConnectionRepo: ClientConnectionRepository,
    private val scope: CoroutineScope,
) : ConnectionRepository {

    override val syncMode = preferencesRepo.preferences.map { it.syncMode }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), SyncMode.Client)

    override val syncEvents: SharedFlow<SyncEvent>
        get() = TODO("Not yet implemented")

    override val connectionState: SharedFlow<ConnectionState>
        get() = TODO("Not yet implemented")

    override val nodesJoined: SharedFlow<HostedSyncGroupNodes>
        get() = TODO("Not yet implemented")

    override val nodesConnected: SharedFlow<HostedSyncGroupNodes>
        get() = TODO("Not yet implemented")

    override val requestedNodes: Flow<Node>
        get() = TODO("Not yet implemented")

    override fun approve(node: Node) {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    override suspend fun send(event: SyncEvent) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleSyncMode() {
        preferencesRepo.setSyncMode(syncMode.value.next())
    }

}
