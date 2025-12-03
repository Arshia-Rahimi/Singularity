package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import kotlinx.coroutines.flow.StateFlow

interface SyncGroupServer {

	val connectedNodes: StateFlow<List<HostedSyncGroupNodeModel>>

	fun start(group: HostedSyncGroupModel)

    fun stop()

}
