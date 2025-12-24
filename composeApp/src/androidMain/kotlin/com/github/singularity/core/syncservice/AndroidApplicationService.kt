package com.github.singularity.core.syncservice

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import org.koin.android.ext.android.inject

class AndroidApplicationService : Service() {

	private val service by inject<SyncService>()

	override fun onBind(intent: Intent?) = null

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		startForeground(1, createNotification())
		return START_STICKY
	}

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
		createNotificationChannel()
		val intent = Intent(this, AndroidApplicationService::class.java)
		this.startForegroundService(intent)
	}
}
