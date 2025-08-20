package com.example.curiate.data.network

import com.example.curiate.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val NEWS_BASE_URL = "https://newsapi.org/v2/"

    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor{chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", BuildConfig.NEWS_API_KEY)
                .build()
            chain.proceed(request)
        }.build()

    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}