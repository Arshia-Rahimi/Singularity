package com.github.singularity.core.client.utils

import io.ktor.serialization.WebsocketContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.charsets.Charset
import io.ktor.websocket.Frame

class SingularityWebsocketContentConverter : WebsocketContentConverter {

    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: Frame,
    ): Any? {
        TODO("Not yet implemented")
    }

    override fun isApplicable(frame: Frame): Boolean {
        TODO("Not yet implemented")
    }
}
