package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import kotlinx.coroutines.flow.StateFlow

interface ServerConnectionRepository {

    val currentServer: StateFlow<HostedSyncGroup?>

    val connectedNodes: StateFlow<List<Node>>

    fun startServer()

    fun stopServer()

}
