package com.github.singularity.core.sync

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.shared.SyncMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ServerSyncService(
    serverConnectionRepo: ServerConnectionRepository,
    preferencesRepo: PreferencesRepository,
    clientConnectionRepo: ClientConnectionRepository,
    hostedSyncGroupRepo: HostedSyncGroupRepository,
    syncEventRepo: SyncEventRepository,
) : SyncService(
    syncEventRepo = syncEventRepo,
) {

    init {
        preferencesRepo.preferences.map { it.syncMode }
            .onEach { mode ->
                when (mode) {
                    SyncMode.Server -> {
                        hostedSyncGroupRepo.syncGroups.collect {
                            val group =
                                it.firstOrNull { group -> group.isDefault } ?: return@collect
                            clientConnectionRepo.stopClient()
                            serverConnectionRepo.startServer(group)
                        }
                    }

                    SyncMode.Client -> {
                        serverConnectionRepo.stopServer()
                        clientConnectionRepo.startClient()
                    }
                }
            }.launchIn(scope)
    }

}
