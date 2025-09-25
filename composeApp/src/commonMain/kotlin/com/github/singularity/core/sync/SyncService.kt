package com.github.singularity.core.sync

import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.flow.StateFlow

interface SyncService {

    val syncMode: StateFlow<SyncMode>

    val connectionState: StateFlow<ConnectionState>

    fun toggleSyncMode()

    fun refreshClient()
    
}
