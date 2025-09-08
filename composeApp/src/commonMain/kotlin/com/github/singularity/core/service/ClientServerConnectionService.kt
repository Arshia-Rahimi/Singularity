package com.github.singularity.core.service

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.shared.SyncMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ClientServerConnectionService(
    serverConnectionRepo: ServerConnectionRepository,
    preferencesRepo: PreferencesRepository,
    clientConnectionRepo: ClientConnectionRepository,
    syncEventRepo: SyncEventRepository,
) : ClientConnectionService(
    syncEventRepo = syncEventRepo,
) {

    init {
        preferencesRepo.preferences.map { it.syncMode }
            .onEach {
                when (it) {
                    SyncMode.Server -> {
                        serverConnectionRepo.stopServer()
                        clientConnectionRepo.startClient()
                    }

                    SyncMode.Client -> {
                        serverConnectionRepo.stopServer()
                        clientConnectionRepo.startClient()
                    }
                }
            }.launchIn(scope)
    }

}
