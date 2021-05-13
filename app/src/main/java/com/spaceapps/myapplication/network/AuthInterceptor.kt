package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.app.AUTH_HEADER
import com.spaceapps.myapplication.app.AUTH_HEADER_PREFIX
import com.spaceapps.myapplication.local.AuthTokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authTokenStorage: AuthTokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val builder = chain.request().newBuilder()
        val token = authTokenStorage.getAuthToken()
        token ?: return@runBlocking chain.proceed(builder.build())
        builder.header(AUTH_HEADER, "$AUTH_HEADER_PREFIX $token")
        return@runBlocking chain.proceed(builder.build())
    }
}
