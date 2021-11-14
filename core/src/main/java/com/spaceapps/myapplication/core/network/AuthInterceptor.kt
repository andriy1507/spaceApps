package com.spaceapps.myapplication.core.network

import com.spaceapps.myapplication.core.AUTH_HEADER
import com.spaceapps.myapplication.core.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.core.local.DataStoreManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val builder = chain.request().newBuilder()
        val token = dataStoreManager.getAccessToken()
        token ?: return@runBlocking chain.proceed(builder.build())
        builder.header(AUTH_HEADER, "$AUTH_HEADER_PREFIX $token")
        return@runBlocking chain.proceed(builder.build())
    }
}
