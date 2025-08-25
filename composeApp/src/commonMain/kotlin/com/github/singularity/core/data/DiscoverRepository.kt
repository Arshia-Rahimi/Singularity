package com.github.singularity.core.data

import com.github.singularity.core.common.util.Resource
import com.github.singularity.core.common.util.Success
import com.github.singularity.core.mdns.Server
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    val discoveredServers: Flow<List<Server>>

    fun sendPairRequest(server: Server): Flow<Resource<Success>>

    fun release()
    
}
