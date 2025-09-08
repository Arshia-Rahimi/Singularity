package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val httpServer: KtorHttpServer,
) : BroadcastRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            
    override val isBroadcasting = httpServer.isServerRunning

    override val syncGroups = hostedSyncGroupRepo.syncGroups

    private val defaultSyncGroup = hostedSyncGroupRepo.defaultGroup
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)
    
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
            startBroadcast()
        }
    }

    override fun approvePairRequest(node: Node) {
        // todo
    }

    override suspend fun startBroadcast() {
        val defaultGroup = defaultSyncGroup.value ?: return
        hostedSyncGroupRepo.setAsDefault(defaultGroup)
        broadcastService.broadcastServer(defaultGroup)
        httpServer.start()
    }

    override fun stopBroadcast() {
        httpServer.stop()
        scope.cancel()
    }

}
