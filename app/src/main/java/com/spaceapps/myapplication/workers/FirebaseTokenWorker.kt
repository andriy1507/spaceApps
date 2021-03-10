package com.spaceapps.myapplication.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.spaceapps.myapplication.local.AuthTokenStorage
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest
import com.spaceapps.myapplication.models.remote.auth.DeviceRequest.Platform.Android
import com.spaceapps.myapplication.network.AuthorizationApi
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FirebaseTokenWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val api: AuthorizationApi,
    private val authTokenStorage: AuthTokenStorage
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        authTokenStorage.getAuthToken() ?: return Result.failure()
        val token = inputData.getString(TOKEN_KEY)
        token ?: return Result.failure()
        val device = DeviceRequest(
            token = token,
            platform = Android
        )
        val response = request { api.addDevice(device = device) }
        return if (response is Success) Result.success() else Result.retry()
    }

    companion object {
        private const val TOKEN_KEY = "FCM_TOKEN"
        fun buildData(token: String) = Data.Builder()
            .putString(TOKEN_KEY, token)
            .build()
    }
}
