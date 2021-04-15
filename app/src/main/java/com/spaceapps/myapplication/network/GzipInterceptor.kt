package com.spaceapps.myapplication.network

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.BufferedSink
import okio.GzipSource
import okio.buffer
import javax.inject.Inject

class GzipInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        originalRequest.body ?: return chain.proceed(originalRequest)
        val compressedRequest = originalRequest.newBuilder()
            .method(originalRequest.method, gzip(originalRequest.body))
            .build()
        val response = chain.proceed(compressedRequest)
        return if (isGzipped(response)) unzip(response) else response
    }

    private fun isGzipped(response: Response) = response.header("Content-Encoding") == "gzip"

    private fun unzip(response: Response): Response {
        val body = response.body
        body ?: return response
        val gzipSource = GzipSource(body.source())
        val bodyString = gzipSource.buffer().readUtf8()
        val responseBody = bodyString.toResponseBody(body.contentType())
        val strippedHeaders = response.headers.newBuilder()
            .removeAll("Content-Encoding")
            .removeAll("Content-Length")
            .build()
        return response.newBuilder()
            .headers(strippedHeaders)
            .body(responseBody)
            .message(response.message)
            .build()
    }

    private fun gzip(body: RequestBody?): RequestBody {
        return object : RequestBody() {
            override fun contentType() = body?.contentType()

            override fun writeTo(sink: BufferedSink) {
                val gzipSink = sink.buffer
                body?.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}
