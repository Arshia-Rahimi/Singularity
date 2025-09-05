package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairRequest

typealias Token = String

interface AuthRepository {

    suspend fun getNode(token: Token): HostedSyncGroupNode?

    suspend fun authenticate(pairRequest: PairRequest): HostedSyncGroupNode?

}
