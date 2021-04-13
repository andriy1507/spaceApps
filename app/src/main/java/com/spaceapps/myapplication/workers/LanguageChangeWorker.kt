package com.spaceapps.myapplication.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.network.SettingsApi
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class LanguageChangeWorker @AssistedInject constructor(
    private val settingsApi: SettingsApi,
    private val authTokenStorage: AuthTokenStorage,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        authTokenStorage.getAuthToken() ?: return@withContext Result.failure()
        when (request { settingsApi.setLanguage(inputData.getString(LANGUAGE_KEY)!!) }) {
            is Success -> Result.success()
            is Error -> Result.retry()
        }
    }

    companion object {
        private const val LANGUAGE_KEY = "languageKey"
        const val WORK_NAME = "languageWork"

        fun provideInputData(language: String) = Data.Builder()
            .putString(LANGUAGE_KEY, language)
            .build()
    }
}
