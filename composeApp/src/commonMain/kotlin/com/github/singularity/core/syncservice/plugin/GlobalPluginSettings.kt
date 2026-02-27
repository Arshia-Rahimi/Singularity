package com.github.singularity.core.syncservice.plugin

import com.github.singularity.core.shared.PluginOption
import com.github.singularity.core.shared.PluginOptionType
import com.github.singularity.core.shared.toPluginOptions

val GlobalPluginSettings = listOf(
    PluginOption(
        name = "isEnabled",
        value = true,
        type = PluginOptionType.Boolean,
    )
).toPluginOptions()
