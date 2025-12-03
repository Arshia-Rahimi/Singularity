package com.github.singularity.core.syncservice

import com.github.singularity.core.shared.SyncMode
import kotlinx.coroutines.flow.StateFlow

interface SyncService {

    val syncMode: StateFlow<SyncMode>

    val syncState: StateFlow<SyncState>

    fun toggleSyncMode()

    fun refresh()

}
