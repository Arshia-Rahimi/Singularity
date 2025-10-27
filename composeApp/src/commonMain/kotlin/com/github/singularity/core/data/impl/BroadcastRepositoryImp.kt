package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val pairRequestRepo: PairRequestRepository,
    private val httpServer: KtorHttpServer,
) : BroadcastRepository {

    override val isBroadcasting = httpServer.isServerRunning

    override val syncGroups = hostedSyncGroupRepo.syncGroups

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupRepo.insert(group)
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
            httpServer.stop()
            httpServer.start(group)
        }
    }

    override fun approvePairRequest(node: Node) {
        pairRequestRepo.approve(node)
    }

    override fun rejectPairRequest(node: Node) {
        pairRequestRepo.reject(node)
    }

    override suspend fun broadcast() = hostedSyncGroupRepo.defaultSyncGroup.flatMapLatest { group ->
        pairRequestRepo.clear()
        broadcastService.stopBroadcast()
        httpServer.stop()

        if (group == null) flowOf(emptyList())
        else {
            httpServer.start(group)
            broadcastService.startBroadcast(group)
            pairRequestRepo.requests.map { list ->
                list.filter { it.status == PairStatus.Awaiting }.map { it.node }
            }
        }

    }.flowOn(Dispatchers.IO)

}
