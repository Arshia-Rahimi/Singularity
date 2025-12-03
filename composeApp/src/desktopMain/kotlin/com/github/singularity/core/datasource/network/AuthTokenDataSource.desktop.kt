package com.github.singularity.core.datasource.network

import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel

interface AuthTokenDataSource {

	suspend fun generateAuthToken(node: NodeModel): HostedSyncGroupNodeModel?

	suspend fun getNode(token: String): HostedSyncGroupNodeModel?

}
