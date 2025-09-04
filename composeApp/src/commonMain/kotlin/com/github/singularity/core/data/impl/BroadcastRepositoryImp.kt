package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import com.github.singularity.data.HostedSyncGroupDataSource
import com.github.singularity.data.entities.HostedSyncGroup
import com.github.singularity.models.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn

class BroadcastRepositoryImp(
    private val broadcastService: DeviceBroadcastService,
    private val hostedSyncGroupsDataSource: HostedSyncGroupDataSource,
    private val scope: CoroutineScope,
) : BroadcastRepository {

    override val syncGroups = hostedSyncGroupsDataSource.hostedSyncGroups
        .shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)

    override fun create(group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.insert(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun editName(groupName: String, group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.updateName(groupName, group.hostedSyncGroupId)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun delete(group: HostedSyncGroup) = flow {
        hostedSyncGroupsDataSource.delete(group)
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun broadcastGroup(group: HostedSyncGroup) = flow {
        broadcastService.broadcastServer(group)
        // todo: run http server and listen for pair requests

        emit(Node("", "", ""))
    }

    override suspend fun setAsDefault(group: HostedSyncGroup) {
        hostedSyncGroupsDataSource.setAsDefault(group)
    }

    override fun approvePairRequest(node: Node) = flow {
        // todo
        emit(Success)
    }.asResult(Dispatchers.IO)

    override fun stopBroadcast() {
        scope.cancel()
    }

}
