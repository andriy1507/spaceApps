package com.spaceapps.myapplication.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.spaceapps.myapplication.network.AuthorizationApi
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FirebaseTokenWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val api: AuthorizationApi
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val token = inputData.getString(TOKEN_KEY)
        token ?: return Result.failure()
        val response = request { api.sendFcmToken(token) }
        return if (response is Success) Result.success() else Result.retry()
    }

    companion object {
        private const val TOKEN_KEY = "FCM_TOKEN"
        fun buildData(token: String) = Data.Builder()
            .putString(TOKEN_KEY, token)
            .build()
    }
}
