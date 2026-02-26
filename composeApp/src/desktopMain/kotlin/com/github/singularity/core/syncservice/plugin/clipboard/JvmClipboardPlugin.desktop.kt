package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.data.PluginSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


@OptIn(ExperimentalCoroutinesApi::class)
class JvmClipboardPlugin(
    pluginSettingsRepo: PluginSettingsRepository,
) : PlatformClipboardPlugin {

    override val systemClipboardUpdatedEvent: Flow<String>
        get() = flowOf()

//    override val systemClipboardUpdatedEvent = pluginSettingsRepo
//        .flatMapLatest { pollDelay ->
//            if (pollDelay == null) emptyFlow()
//            else flow {
//                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
//                var lastContent: Transferable? = null
//                while (true) {
//                    try {
//                        val content = clipboard.getContents(null)
//                        if (content != null && content != lastContent) {
//                            if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                                lastContent = content
//                                (lastContent.getTransferData(DataFlavor.stringFlavor) as? String)?.let {
//                                    emit(it)
//                                }
//                            }
//                        }
//                    } catch (_: Exception) {
//                    }
//                    delay(pollDelay.toLong())
//                }
//            }
//        }

    override fun copy(content: String) {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(content)
            clipboard.setContents(selection, null)
        } catch (_: Exception) {
        }
    }

}
