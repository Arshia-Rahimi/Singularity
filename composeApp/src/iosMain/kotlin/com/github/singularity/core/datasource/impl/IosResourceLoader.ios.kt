package com.github.singularity.core.datasource.impl

import com.github.singularity.core.datasource.ResourceLoader
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

class IosResourceLoader : ResourceLoader {

	@OptIn(InternalResourceApi::class)
	override suspend fun loadFile(fileName: String) = readResourceBytes(fileName)

}
