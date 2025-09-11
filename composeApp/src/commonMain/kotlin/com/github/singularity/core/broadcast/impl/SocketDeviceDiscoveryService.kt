package com.github.singularity.core.broadcast.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.runningFold

class SocketDeviceDiscoveryService : DeviceDiscoveryService {

    override val discoveredServers = flow {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager).udp().bind(InetSocketAddress("0.0.0.0", SERVER_PORT))
        while (true) {
            val datagram = socket.receive()
            val message = datagram.packet.readText() // todo
            val senderIp = datagram.address.toString()

            emit(
                LocalServer(
                    ip = senderIp,
                    deviceName = "",
                    deviceId = "",
                    deviceOs = "",
                    syncGroupName = "",
                    syncGroupId = "",
                )
            )
        }
    }.runningFold(emptyList<LocalServer>()) { list, newServer -> list + newServer }

    override suspend fun discoverServer(syncGroup: JoinedSyncGroup): LocalServer? {
        TODO("Not yet implemented")
    }

}
