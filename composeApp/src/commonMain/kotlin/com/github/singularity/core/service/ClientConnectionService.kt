package com.github.singularity.core.service

import com.github.singularity.core.data.SyncEventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

open class ClientConnectionService(
    private val syncEventRepo: SyncEventRepository,
) {

    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // todo handle plugins and pass the events to them

}
