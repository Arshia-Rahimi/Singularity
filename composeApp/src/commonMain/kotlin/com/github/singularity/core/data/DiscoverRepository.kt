package com.github.singularity.core.data

import com.github.singularity.core.mdns.Server
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    val discoveredServers: Flow<List<Server>>

    fun sendPairRequest(server: Server): Flow<Resource<Success>>

    fun release()
    
}
