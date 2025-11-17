package com.github.singularity.core.datasource.database

import kotlin.enums.enumEntries

fun Boolean.toLong() = if (this) 1L else 0L

fun Long.toBoolean() = this != 0L

inline fun <reified T : Enum<T>> Long.toEnum(): T = enumEntries<T>()[this.toInt()]
