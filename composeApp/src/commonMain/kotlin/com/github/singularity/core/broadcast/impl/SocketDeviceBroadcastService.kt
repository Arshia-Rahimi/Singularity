package com.github.singularity.core.broadcast.impl

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class SocketDeviceBroadcastService : DeviceBroadcastService {

    override suspend fun startBroadcast(group: HostedSyncGroup) {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).udp().bind(InetSocketAddress("0.0.0.0", 0))
        val message = ""
        socket.send(
            Datagram(
                ByteReadPacket(message.encodeToByteArray()),
                InetSocketAddress("255.255.255.255", SERVER_PORT)
            )
        )
        socket.close()
    }

    override suspend fun stopBroadcast() = Unit

}
