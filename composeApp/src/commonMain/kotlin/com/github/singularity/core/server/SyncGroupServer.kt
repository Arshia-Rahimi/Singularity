package com.github.singularity.core.server

import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import kotlinx.coroutines.flow.StateFlow

interface SyncGroupServer {

    val connectedNodes: StateFlow<List<HostedSyncGroupNode>>

    fun start(group: HostedSyncGroup)

    fun stop()

}