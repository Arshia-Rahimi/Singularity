package com.github.singularity.core.sync

import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.model.SyncState
import kotlinx.coroutines.flow.StateFlow

interface SyncService {

    val syncMode: StateFlow<SyncMode>

    val syncState: StateFlow<SyncState>

    fun toggleSyncMode()

    fun refresh()
    
}
