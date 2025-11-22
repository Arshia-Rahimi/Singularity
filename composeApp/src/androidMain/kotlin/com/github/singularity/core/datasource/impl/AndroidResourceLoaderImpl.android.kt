package com.github.singularity.core.datasource.impl

import android.content.Context
import com.github.singularity.core.datasource.ResourceLoader

class AndroidResourceLoaderImpl(
	private val context: Context,
) : ResourceLoader {

	override suspend fun loadFile(fileName: String) =
		context.assets.open(fileName).use { it.readBytes() }

}
