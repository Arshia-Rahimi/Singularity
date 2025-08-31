package com.github.singularity.core.data

import com.github.singularity.core.shared.model.ConnectionState
import kotlinx.coroutines.flow.SharedFlow

interface ConnectionRepository {
    val connection: SharedFlow<ConnectionState>
}
