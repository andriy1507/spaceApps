package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadersInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(USER_AGENT, System.getProperty("http.agent"))
            .header(HOST, BuildConfig.SERVER_URL.substringAfter("://"))
            .header(ACCEPT_ENCODING, "gzip")
            .header(CONTENT_ENCODING, "gzip")
            .header(CACHE_CONTROL, "no-cache")
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val USER_AGENT = "User-Agent"
        private const val HOST = "Host"
        private const val ACCEPT_ENCODING = "Accept-Encoding"
        private const val CONTENT_ENCODING = "Content-Encoding"
        private const val CACHE_CONTROL = "Cache-Control"
    }
}
