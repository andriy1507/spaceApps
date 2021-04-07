package com.spaceapps.myapplication

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.spaceapps.myapplication.workers.FirebaseTokenWorker
import kotlinx.coroutines.runBlocking
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

    private fun buildCustomNotification(
        data: Map<String, String>,
        intent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .apply {
                setContentText(data["text"])
                setSmallIcon(R.drawable.ic_notifications_active)
                data["imageUrl"]?.let { imageUrl ->
                    val request = ImageRequest.Builder(this@SpaceAppsFcmService)
                        .data(imageUrl)
                        .build()
                    val image =
                        runBlocking { ImageLoader(this@SpaceAppsFcmService).execute(request) }
                    setLargeIcon(image.drawable?.toBitmap())
                }
                setContentTitle(data["title"])
                priority = NotificationCompat.PRIORITY_MAX
                setChannelId(getString(R.string.default_notification_channel_id))
                setAutoCancel(true)
                setContentIntent(intent)
            }.build()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d(message.data.toString())
        val intent = PendingIntent.getActivity(
            this@SpaceAppsFcmService,
            Random.nextInt(),
            Intent(this, SpaceAppsMainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = buildCustomNotification(message.data, intent)
        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
    }

    companion object {
        private const val FCM_TOKEN_WORK = "fcmTokenWork"
    }
}
