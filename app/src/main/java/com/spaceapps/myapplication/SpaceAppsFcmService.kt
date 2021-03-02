package com.spaceapps.myapplication

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.graphics.drawable.toBitmap
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.local.SettingsStorage
import com.spaceapps.myapplication.workers.FirebaseTokenWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class SpaceAppsFcmService : FirebaseMessagingService() {

    @Inject
    lateinit var authTokenStorage: AuthTokenStorage

    @Inject
    lateinit var settingsStorage: SettingsStorage

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannelCompat.Builder(
            getString(R.string.default_notification_channel_id),
            IMPORTANCE_HIGH
        ).setName(getString(R.string.default_notification_channel_id)).build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        Timber.tag("FCM Token").d(token)
        CoroutineScope(Dispatchers.Default).launch {
            authTokenStorage.storeFcmToken(token)
            if (authTokenStorage.getAuthToken() != null) {
                val request = OneTimeWorkRequestBuilder<FirebaseTokenWorker>()
                    .setInputData(FirebaseTokenWorker.buildData(token))
                    .build()
                WorkManager.getInstance(this@SpaceAppsFcmService).enqueue(request)
            }
        }
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
                    val request = ImageRequest.Builder(this@SpaceAppsFcmService)
                        .data(imageUrl)
                        .build()
                    val image =
                        runBlocking { ImageLoader(this@SpaceAppsFcmService).execute(request) }
                    setLargeIcon(image.drawable?.toBitmap())
                }
                setContentTitle(message.data["title"])
                priority = NotificationCompat.PRIORITY_MAX
                setChannelId(getString(R.string.default_notification_channel_id))
                setContentIntent(intent)
            }.build()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val enabled = runBlocking { settingsStorage.getNotificationsEnabled() }
        if (!enabled) return
        val intent = PendingIntent.getActivity(
            this@SpaceAppsFcmService,
            Random.nextInt(),
            Intent(this, SpaceAppsMainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = buildCustomNotification(message, intent)
        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
    }
}
