package com.spaceapps.myapplication

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.stetho.Stetho
import com.github.venom.Venom
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SpaceAppsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
        val venom = Venom.createInstance(this)
        venom.initialize()
        venom.start()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
