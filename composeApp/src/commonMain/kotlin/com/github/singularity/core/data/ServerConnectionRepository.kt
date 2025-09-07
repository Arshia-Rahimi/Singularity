package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ServerConnectionRepository {

    val defaultServer: SharedFlow<HostedSyncGroup?>

    val isWebSocketServerActive: StateFlow<Boolean>

    val connectedNodes: StateFlow<List<Node>>

    fun startServer()

    fun stopServer()

}
