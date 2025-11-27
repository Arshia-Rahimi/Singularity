package com.github.singularity.core.syncservice.plugin.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class AndroidClipboardPlugin(
	private val context: Context,
) : PlatformClipboardPlugin {

	override fun copy(content: String) {
		val clipboardManager =
			context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clipData = ClipData.newPlainText(content, content)
		clipboardManager.setPrimaryClip(clipData)
	}

}
