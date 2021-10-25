package com.spaceapps.myapplication.app

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import coil.util.Logger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class SpaceAppsApplication : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient(okHttpClient.newBuilder().cache(CoilUtils.createDefaultCache(this)).build())
        .logger(
            object : Logger {
                override var level: Int = Log.DEBUG

                override fun log(
                    tag: String,
                    priority: Int,
                    message: String?,
                    throwable: Throwable?
                ) {
                    Log.println(priority, tag, message.orEmpty())
                }
            }
        ).build()
}
