package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.local.AuthTokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

const val AUTH_HEADER = "Authorization"
const val AUTH_HEADER_PREFIX = "Bearer "

class AuthInterceptor @Inject constructor(
    private val authTokenStorage: AuthTokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        authTokenStorage.authToken?.let {
            builder.addHeader(AUTH_HEADER, AUTH_HEADER_PREFIX + it)
        }
        return chain.proceed(builder.build())
    }
}