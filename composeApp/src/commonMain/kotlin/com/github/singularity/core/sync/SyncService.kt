package com.github.singularity.core.sync

import com.github.singularity.core.data.SyncEventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

open class SyncService(
    private val syncEventRepo: SyncEventRepository,
) {

    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // todo handle plugins and pass the events to them

}
