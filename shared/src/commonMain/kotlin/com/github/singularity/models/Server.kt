package com.github.singularity.models

import kotlinx.serialization.Serializable

interface IServer {
    val ip: String
    val syncGroupName: String
    val syncGroupId: String
}

@Serializable
class Server(
    override val ip: String,
    override val syncGroupName: String,
    override val syncGroupId: String,
) : IServer
