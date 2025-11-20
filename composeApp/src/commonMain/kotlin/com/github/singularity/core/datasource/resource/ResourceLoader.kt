package com.github.singularity.core.datasource.resource

interface ResourceLoader {

	suspend fun loadFile(fileName: String): ByteArray

}
