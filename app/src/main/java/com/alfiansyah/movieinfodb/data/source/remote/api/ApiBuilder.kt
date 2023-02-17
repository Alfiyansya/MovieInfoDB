package com.alfiansyah.movieinfodb.data.source.remote.api

import com.alfiansyah.movieinfodb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiBuilder {
    private var retrofit: Retrofit = buildRetrofit(httpClient)
    fun createService(): NetworkService = retrofit.create(NetworkService::class.java)

    private val httpClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            var builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
                connectTimeout(5, TimeUnit.SECONDS)
                readTimeout(5, TimeUnit.SECONDS)
            }
            if (BuildConfig.DEBUG) {
                builder = builder.addInterceptor(loggingInterceptor)
            }
            return builder.build()
        }

    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}