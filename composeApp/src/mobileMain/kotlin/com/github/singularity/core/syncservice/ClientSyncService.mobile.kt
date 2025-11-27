package com.github.singularity.core.syncservice

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.model.SyncState
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.plugin.PluginEventHandler
import com.github.singularity.core.syncservice.plugin.PluginEventHandlerImpl
import com.github.singularity.core.syncservice.plugin.PluginWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClientSyncService(
	private val clientConnectionRepo: ClientConnectionRepository,
	pluginSettingsRepo: PluginSettingsRepository,
	syncEventBridge: SyncEventBridge,
	pluginWrapper: PluginWrapper,
) : SyncService,
	PluginEventHandler by PluginEventHandlerImpl(
		pluginSettingsRepo,
		syncEventBridge,
		pluginWrapper,
	) {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override val syncMode = MutableStateFlow(SyncMode.Client).asStateFlow()

	override val syncState: StateFlow<SyncState> = clientConnectionRepo.connectionState
		.stateInWhileSubscribed(ClientSyncState.NoDefaultServer, scope)

	override fun toggleSyncMode() = Unit

	override fun refresh() {
		scope.launch {
			clientConnectionRepo.refresh()
		}
	}

}
