package com.github.singularity.core.syncservice.plugin.clipboard

import com.github.singularity.core.datasource.database.PluginSettingsData

object DesktopClipboardPluginSettingsData {
    val clipboardPollDelay = "desktop_clipboardPollDelay" to null
}

actual val platformClipboardPluginSettingsData: PluginSettingsData = mapOf(
    DesktopClipboardPluginSettingsData.clipboardPollDelay,
)
