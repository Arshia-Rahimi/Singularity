package com.github.singularity.core.datasource

interface ResourceLoader {

    suspend fun loadFile(fileName: String): ByteArray

}