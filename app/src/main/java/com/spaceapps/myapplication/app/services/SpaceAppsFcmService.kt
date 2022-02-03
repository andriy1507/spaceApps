package com.spaceapps.myapplication.app.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.activity.MainActivity
import com.spaceapps.myapplication.app.workers.FirebaseTokenWorker
import com.spaceapps.myapplication.core.*
import timber.log.Timber
import kotlin.random.Random

class SpaceAppsFcmService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannelCompat.Builder(
            getString(R.string.default_notification_channel_id),
            NotificationManagerCompat.IMPORTANCE_HIGH
        ).setName(getString(R.string.app_name)).build()
        NotificationManagerCompat.from(this)
            .createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        Timber.tag("FCM Token").d(token)
        val request = OneTimeWorkRequestBuilder<FirebaseTokenWorker>()
            .setInputData(FirebaseTokenWorker.buildData(token))
            .build()
        WorkManager.getInstance(this@SpaceAppsFcmService)
            .enqueueUniqueWork(FCM_TOKEN_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    private fun buildNotification(data: Map<String, String>): Notification {
        return NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .apply {
                when (data[NOTIFICATION_TYPE]) {
                    NOTIFICATION_NEW_LOGIN -> buildNewLoginNotification(data)
                }
                setSmallIcon(R.drawable.ic_launcher_foreground)
                color = ContextCompat.getColor(this@SpaceAppsFcmService, R.color.colorPrimary)
                priority = NotificationCompat.PRIORITY_MAX
                setChannelId(getString(R.string.default_notification_channel_id))
                setAutoCancel(true)
            }.build()
    }

    private fun NotificationCompat.Builder.buildNewLoginNotification(data: Map<String, String>) {
        setContentIntent(
            PendingIntent.getActivity(
                this@SpaceAppsFcmService,
                Random.nextInt(),
                Intent(this@SpaceAppsFcmService, MainActivity::class.java),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        setContentTitle(getString(R.string.new_login))
        setContentText(
            getString(
                R.string.new_login_text,
                data[NOTIFICATION_DEVICE_MANUFACTURER],
                data[NOTIFICATION_DEVICE_MODEL],
                data[NOTIFICATION_DEVICE_OS_VERSION]
            )
        )
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d(message.data.toString())
        NotificationManagerCompat.from(this).notify(Random.nextInt(), buildNotification(message.data))
    }

    companion object {
        private const val FCM_TOKEN_WORK = "fcmTokenWork"
    }
}
