package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow

class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val scope: CoroutineScope,
) : BroadcastRepository {

    override val syncGroups = hostedSyncGroupRepo.syncGroups

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.create(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun editName(groupName: String, group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.editName(groupName, group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun delete(group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.delete(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override suspend fun setAsDefault(group: HostedSyncGroup) =
        hostedSyncGroupRepo.setAsDefault(group)

    override fun broadcastGroup(group: HostedSyncGroup) = flow {
        broadcastService.broadcastServer(group)
        // todo: run http server and listen for pair requests

        emit(Node("", "", ""))
    }

    override fun approvePairRequest(node: Node) = flow {
        // todo
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun stopBroadcast() {
        scope.cancel()
    }

}
