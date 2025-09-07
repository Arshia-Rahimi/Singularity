package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.flow.SharedFlow

interface ClientConnectionRepository {

    val connectionState: SharedFlow<ConnectionState>

    fun refresh()

}
