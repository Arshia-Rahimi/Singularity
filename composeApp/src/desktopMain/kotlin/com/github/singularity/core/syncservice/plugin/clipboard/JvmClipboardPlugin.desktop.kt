package com.github.singularity.core.syncservice.plugin.clipboard

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class JvmClipboardPlugin : PlatformClipboardPlugin {

	override fun copy(content: String) {
		val clipboard = Toolkit.getDefaultToolkit().systemClipboard
		val selection = StringSelection(content)
		clipboard.setContents(selection, null)
	}

}
