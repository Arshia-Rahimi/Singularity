package com.github.singularity.core.client

import com.github.singularity.models.Server
import com.github.singularity.models.websocket.WebsocketResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketClientDataSource {

    fun connect(server: Server, authKey: String): Flow<WebsocketResponse>

}
