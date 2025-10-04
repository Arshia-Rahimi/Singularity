package com.github.singularity.core.shared

import android.os.Build
import java.util.Locale.getDefault

actual val deviceName: String
    get() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer, ignoreCase = true)) {
            model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
        } else {
            "${model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }} $model"
        }
    }
