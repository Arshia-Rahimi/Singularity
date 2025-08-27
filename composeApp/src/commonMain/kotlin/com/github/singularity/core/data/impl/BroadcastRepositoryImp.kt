package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.database.LocalHostedSyncGroupsDataSource
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.mdns.Node
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn

class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupsRepo: LocalHostedSyncGroupsDataSource,
    private val scope: CoroutineScope,
) : BroadcastRepository {

    override val syncGroups = hostedSyncGroupsRepo.hostedSyncGroups
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupsRepo.insert(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun delete(group: HostedSyncGroup) = flow {
        hostedSyncGroupsRepo.delete(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun broadcastGroup(group: HostedSyncGroup) = flow {
        hostedSyncGroupsRepo.setAsDefault(group)
        broadcastService.broadcastServer(group)
        // todo: run http server and listen for pair requests

        emit(Node("", "", "", ""))
    }

    override fun approvePairRequest(node: Node) = flow {
        // todo
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun stopBroadcast() {
        scope.cancel()
    }

}
