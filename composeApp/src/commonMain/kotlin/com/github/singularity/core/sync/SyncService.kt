package com.github.singularity.core.sync

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

open class SyncService(
    private val syncEventRepo: SyncEventRepository,
    clientConnectionRepo: ClientConnectionRepository,
) {

    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        clientConnectionRepo.startClient()
    }

    // todo handle plugins and pass the events to them

}
