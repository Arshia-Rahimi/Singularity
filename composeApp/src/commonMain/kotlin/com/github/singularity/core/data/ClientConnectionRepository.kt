package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ClientConnectionState
import kotlinx.coroutines.flow.Flow

interface ClientConnectionRepository {

    val connectionState: Flow<ClientConnectionState>

    fun refresh()

}
