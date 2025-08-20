package com.example.curiate.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    suspend fun getNewsByQuery(@Query("q") query: String): Response<NewsSearchResponseDto>

    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String): Response<NewsSearchResponseDto>
}