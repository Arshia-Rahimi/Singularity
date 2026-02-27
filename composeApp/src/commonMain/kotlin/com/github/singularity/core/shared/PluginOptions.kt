package com.github.singularity.core.shared

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class PluginOptions(
    val options: List<PluginOption>,
)

fun List<PluginOption>.toPluginOptions() = PluginOptions(this)

@Serializable
data class PluginOption(
    val name: String,
    val value: String,
    val type: PluginOptionType,
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> getValue() = when (type) {
        PluginOptionType.String -> value
        PluginOptionType.Int -> value.toInt()
        PluginOptionType.Long -> value.toLong()
        PluginOptionType.Boolean -> value.toBoolean()
    } as T
}

enum class PluginOptionType {
    String,
    Int,
    Long,
    Boolean,
}
