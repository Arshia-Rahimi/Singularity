package com.github.singularity.core.data

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    val discoveredServers: Flow<List<LocalServer>>

    fun sendPairRequest(server: LocalServer): Flow<Resource<Success>>

    fun release()
    
}
