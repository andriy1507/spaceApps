package com.spaceapps.myapplication.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.results.AddDeviceResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class FirebaseTokenWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        dataStoreManager.getAccessToken() ?: return@withContext Result.failure()
        val token = inputData.getString(TOKEN_KEY)
        token ?: return@withContext Result.failure()
        return@withContext when (authRepository.addDevice(token = token)) {
            AddDeviceResult.Success -> Result.success()
            AddDeviceResult.Failure -> Result.retry()
        }
    }

    companion object {
        private const val TOKEN_KEY = "FCM_TOKEN"
        fun buildData(token: String) = Data.Builder()
            .putString(TOKEN_KEY, token)
            .build()
    }
}
