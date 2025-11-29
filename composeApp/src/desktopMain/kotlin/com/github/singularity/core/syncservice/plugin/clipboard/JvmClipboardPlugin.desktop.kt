package com.github.singularity.core.syncservice.plugin.clipboard

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable

class JvmClipboardPlugin : PlatformClipboardPlugin {

	override val systemClipboardUpdatedEvent = flow {
		val clipboard = Toolkit.getDefaultToolkit().systemClipboard
		var lastContent: Transferable? = null
		while (true) {
			try {
				val content = clipboard.getContents(null)
				if (content != null && content != lastContent) {
					if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						lastContent = content
						(lastContent.getTransferData(DataFlavor.stringFlavor) as? String)?.let {
							emit(it)
						}
					}
				}
			} catch (_: Exception) {
			}
			delay(3_000L)
		}
	}

	override fun copy(content: String) {
		println(content)
		try {
			val clipboard = Toolkit.getDefaultToolkit().systemClipboard
			val selection = StringSelection(content)
			clipboard.setContents(selection, null)
		} catch (_: Exception) {
		}
	}

}
