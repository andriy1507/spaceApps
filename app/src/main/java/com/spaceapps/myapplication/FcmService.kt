package com.spaceapps.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.workers.FirebaseTokenWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(this)
                .createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        Timber.tag("FCM Token").d(token)
        authTokenStorage.storeFcmToken(token)
        if (authTokenStorage.authToken != null) {
            val request = OneTimeWorkRequestBuilder<FirebaseTokenWorker>()
                .setInputData(FirebaseTokenWorker.buildData(token))
                .build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

    private fun buildFirebaseNotification(
        it: RemoteMessage.Notification,
        intent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id)).apply {
            setContentTitle(it.title)
            setContentText(it.body)
            setSmallIcon(R.drawable.ic_notifications_active)
            priority = NotificationCompat.PRIORITY_MAX
            setChannelId(getString(R.string.default_notification_channel_id))
            setContentIntent(intent)
        }.build()
    }

    private fun buildCustomNotification(
        message: RemoteMessage,
        intent: PendingIntent
    ): Notification {
        return NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .apply {
                setContentText(message.data["text"])
                setSmallIcon(R.drawable.ic_notifications_active)
                message.data["imageUrl"]?.let { imageUrl ->
                    val request = ImageRequest.Builder(this@FcmService)
                        .data(imageUrl)
                        .build()
                    val image = runBlocking { ImageLoader(this@FcmService).execute(request) }
                    setLargeIcon(image.drawable?.toBitmap())
                }
                setContentTitle(message.data["title"])
                priority = NotificationCompat.PRIORITY_MAX
                setChannelId(getString(R.string.default_notification_channel_id))
                setContentIntent(intent)
            }.build()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val intent = PendingIntent.getActivity(
            this@FcmService,
            Random.nextInt(),
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = message.notification?.let { buildFirebaseNotification(it, intent) }
            ?: run { buildCustomNotification(message, intent) }
        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
    }
}