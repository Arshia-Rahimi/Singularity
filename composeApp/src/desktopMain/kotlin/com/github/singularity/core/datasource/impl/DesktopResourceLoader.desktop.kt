package com.github.singularity.core.datasource.impl

import com.github.singularity.core.datasource.ResourceLoader

class DesktopResourceLoader : ResourceLoader {

	override suspend fun loadFile(fileName: String) =
		Thread.currentThread().contextClassLoader
			.getResourceAsStream(fileName)
			?.readBytes()
			?: error("$fileName not found")

}
