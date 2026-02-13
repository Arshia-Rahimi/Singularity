package com.github.singularity.components

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.singularity.MainActivity

const val NOTIFICATION_CHANNEL_ID = "android_sync_service"

fun Context.createNotificationChannel() {
	val channel = NotificationChannel(
		NOTIFICATION_CHANNEL_ID,
		"Android SyncService",
		NotificationManager.IMPORTANCE_LOW,
	)

	(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
		.createNotificationChannel(channel)
}

fun Context.createNotification(): Notification {
	val intent = Intent(this, MainActivity::class.java)
	val pendingIntent = PendingIntent.getActivity(
		this,
		0,
		intent,
		PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
	)

	return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
		.setContentTitle("Singularity")
		.setContentText("Sync Service Running...")
		.setContentIntent(pendingIntent)
		.setSmallIcon(R.drawable.ic_popup_sync)
		.setOngoing(true)
		.build()
}
