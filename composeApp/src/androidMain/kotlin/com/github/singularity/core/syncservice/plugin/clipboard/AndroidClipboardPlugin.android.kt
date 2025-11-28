package com.github.singularity.core.syncservice.plugin.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class AndroidClipboardPlugin(
	context: Context,
) : PlatformClipboardPlugin {

	val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

	override val systemClipboardUpdatedEvent = callbackFlow {
		val listener = ClipboardManager.OnPrimaryClipChangedListener {
			val newClip = clipboardManager.primaryClip
			if (newClip != null && newClip.itemCount > 0) {
				val copiedText = newClip.getItemAt(0).text
				trySend(copiedText.toString())
			}
		}
		clipboardManager.addPrimaryClipChangedListener(listener)

		awaitClose {
			clipboardManager.removePrimaryClipChangedListener(listener)
		}
	}

	override fun copy(content: String) {
		val clipData = ClipData.newPlainText(content, content)
		clipboardManager.setPrimaryClip(clipData)
	}

}
