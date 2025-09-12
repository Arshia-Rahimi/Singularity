package com.github.singularity.core.broadcast.nsd

internal val String.qualified
    get() = if (this.endsWith(".")) this else "$this."

internal val String.localQualified
    get() = if (this.endsWith(".local.")) this else "${this.qualified}local."

internal val String.stripLocal
    get() = this.removeSuffix(".local.").removeSuffix(".local")
