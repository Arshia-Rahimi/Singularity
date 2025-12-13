package com.github.singularity.core.syncservice

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import org.koin.android.ext.android.inject

class AndroidApplicationService : Service() {

    private val service by inject<SyncService>()

    override fun onBind(intent: Intent?) = null

}

fun Context.startAndroidApplicationService() {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.FOREGROUND_SERVICE
        ) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val intent = Intent(this, AndroidApplicationService::class.java)
        this.startForegroundService(intent)
    } else {
        Log.e("MyService", "No permissions!")
    }
}
