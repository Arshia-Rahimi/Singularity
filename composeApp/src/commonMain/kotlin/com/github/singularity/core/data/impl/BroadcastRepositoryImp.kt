package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val pairRequestRepo: PairRequestRepository,
    private val httpServer: KtorHttpServer,
) : BroadcastRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            
    override val isBroadcasting = httpServer.isServerRunning

    override val pairRequests = pairRequestRepo.requests
        .map { list -> list.filter { it.status == PairStatus.Awaiting }.map { it.node } }

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

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupRepo.setAsDefault(group)
        if (isBroadcasting.value) {
            stopBroadcast()
            httpServer.stop()
            startBroadcast()
            httpServer.start(group)
        }
    }

    override fun approvePairRequest(node: Node) {
        pairRequestRepo.approve(node)
    }

    override fun rejectPairRequest(node: Node) {
        pairRequestRepo.reject(node)
    }

    override suspend fun startBroadcast() {
        val defaultGroup = syncGroups.first().firstOrNull { it.isDefault } ?: return
        httpServer.start(defaultGroup)
        scope.launch {
            broadcastService.broadcastServer(defaultGroup)
        }
    }

    override suspend fun stopBroadcast() {
        scope.cancel()
        httpServer.stop()
    }

}
