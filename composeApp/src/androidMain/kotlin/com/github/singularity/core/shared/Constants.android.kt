package com.github.singularity.core.shared

import android.os.Build

actual val platform = "Android"

actual val os = platform + Build.VERSION.RELEASE
