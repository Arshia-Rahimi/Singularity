package com.github.singularity.core.database

fun Boolean.toLong() = if (this) 1L else 0L

fun Long.toBoolean() = this != 0L
