package com.github.singularity.core.shared

import io.ktor.util.reflect.instanceOf
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

@JvmInline
@Serializable
value class PluginSettings(
    val options: List<PluginOption>,
)

fun List<PluginOption>.toPluginOptions() = PluginSettings(this)

@ConsistentCopyVisibility
@Serializable
data class PluginOption private constructor(
    val name: String,
    val value: String,
    val type: PluginOptionType,
) {

    private fun checkValueType(value: Any, type: PluginOptionType) {
        if(!value.instanceOf(type.typeClass)) throw WrongPluginOptionValueTypeException(type, value::class)
    }

    constructor(
        name: String,
        value: Any,
        type: PluginOptionType,
    ) : this(name, value.toString(), type) {
        checkValueType(value, type)
    }

    fun updateValue(value: Any): PluginOption {
        checkValueType(value, type)
        return copy(value = value.toString())
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue() = when (type) {
        PluginOptionType.String -> value
        PluginOptionType.Int -> value.toInt()
        PluginOptionType.Long -> value.toLong()
        PluginOptionType.Boolean -> value.toBoolean()
    } as T

}

enum class PluginOptionType(val typeClass: KClass<*>) {
    String(typeClass = kotlin.String::class),
    Int(typeClass = kotlin.Int::class),
    Long(typeClass = kotlin.Long::class),
    Boolean(typeClass = kotlin.Boolean::class),
}

class WrongPluginOptionValueTypeException(expected: PluginOptionType, received: KClass<*>): Exception() {
    override val message = "Wrong plugin option value type. Expected: ${expected.typeClass}, received: $received."
}
