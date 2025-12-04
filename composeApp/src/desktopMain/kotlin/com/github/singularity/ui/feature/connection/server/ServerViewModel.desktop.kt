package com.github.singularity.ui.feature.connection.server

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.ServerSyncState
import com.github.singularity.core.syncservice.SyncService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ServerViewModel(
	private val broadcastRepo: BroadcastRepository,
	private val syncService: SyncService,
) : ViewModel() {

	private val connectionState = syncService.syncState
		.filterIsInstance<ServerSyncState>()
		.stateInWhileSubscribed(ServerSyncState.Loading)

	private val hostedSyncGroups = broadcastRepo.syncGroups
		.map {
			it.map { hostedSyncGroupModel -> hostedSyncGroupModel.toHostedSyncGroup() }
				.distinctBy { hostedSyncGroup -> hostedSyncGroup.groupId }
		}.stateInWhileSubscribed(emptyList())

	val uiState = combine(
		connectionState,
		hostedSyncGroups,
	) { connectionState, hostedSyncGroups ->
		ServerUiState(
			connectionState = connectionState,
			hostedSyncGroups = hostedSyncGroups.toMutableStateList(),
		)
	}.stateInWhileSubscribed(ServerUiState())

	fun execute(intent: ServerIntent) = when (intent) {
		is ServerIntent.Approve -> broadcastRepo.approvePairRequest(intent.node.deviceId)
		is ServerIntent.Reject -> broadcastRepo.rejectPairRequest(intent.node.deviceId)
		is ServerIntent.CreateGroup -> create(intent.groupName)
		is ServerIntent.EditGroupName -> editName(intent.groupName, intent.group)
		is ServerIntent.DeleteGroup -> delete(intent.group)
		is ServerIntent.SetAsDefault -> setAsDefault(intent.group)
		is ServerIntent.RefreshConnection -> syncService.refresh()
		is ServerIntent.ToIndex -> toIndex()
	}

	private fun setAsDefault(group: HostedSyncGroup) {
		viewModelScope.launch {
			broadcastRepo.setAsDefault(group.groupId)
		}
	}

	private fun create(groupName: String) {
		viewModelScope.launch {
			broadcastRepo.create(HostedSyncGroupModel(groupName))
		}
	}

	private fun editName(groupName: String, group: HostedSyncGroup) {
		viewModelScope.launch {
			broadcastRepo.editName(groupName, group.groupId)
		}
	}

	private fun delete(group: HostedSyncGroup) {
		viewModelScope.launch {
			broadcastRepo.delete(group.groupId)
		}
	}

	private fun toIndex() {
		viewModelScope.launch {
			broadcastRepo.removeAllDefaults()
		}
	}

}
