package com.example.curiate.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    fun getNewsByQuery(
        @Header("X-Api-Key") apiKey: String,
        @Query("q") query: String
    ): Call<NewsSearchResponseDto>

    @GET("top-headlines")
    fun getTopHeadlines(
        @Header("X-Api-Key") apiKey: String,
        @Query("country") country: String = "us"
    ): Call<NewsSearchResponseDto>
}