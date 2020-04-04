package com.andriiz.api

import com.andriiz.domain.contract.Provider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class OkHTTPClientProvider(private val authToken: String) : Provider<OkHttpClient> {

    private val requestInterceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.addHeader(AUTHORIZATION, "$BEARER $authToken")
        chain.proceed(requestBuilder.build())
    }

    override fun get(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        addInterceptor(requestInterceptor)
    }.build()

    companion object {
        private const val TIME_OUT = 5000L
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }

}