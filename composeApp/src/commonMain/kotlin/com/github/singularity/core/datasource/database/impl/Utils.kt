package com.github.singularity.core.datasource.database.impl

import app.cash.sqldelight.ColumnAdapter
import com.github.singularity.core.shared.PluginOptions
import kotlinx.serialization.json.Json
import kotlin.enums.enumEntries

fun Boolean.toLong() = if (this) 1L else 0L

fun Long.toBoolean() = this != 0L

inline fun <reified T : Enum<T>> Long.toEnum(): T = enumEntries<T>()[this.toInt()]

val pluginSettingsAdapter = object : ColumnAdapter<PluginOptions, String> {
    override fun decode(databaseValue: String) = Json.decodeFromString<PluginOptions>(databaseValue)
    override fun encode(value: PluginOptions) = Json.encodeToString(value)
}
