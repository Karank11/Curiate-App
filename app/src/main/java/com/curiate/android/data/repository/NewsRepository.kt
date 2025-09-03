package com.curiate.android.data.repository

import com.curiate.android.data.network.NewsApiService
import com.curiate.android.domain.models.NewsArticle
import com.curiate.android.domain.models.newsResponseDtoMapToNewsArticle

class NewsRepository(private val newsApiService: NewsApiService) {

    suspend fun getNewsByQuery(query: String): List<NewsArticle> {
        val newsResponse = newsApiService.getNewsByQuery(query)
        if (newsResponse.isSuccessful && newsResponse.body() != null) {
            val responseBody = newsResponse.body() ?: throw Exception("Empty response body")
            val articleList = responseBody.articles
            if (articleList.isNotEmpty()) {
                return newsResponseDtoMapToNewsArticle(articleList)
            }
            throw Exception("No articles found")
        } else {
            throw Exception("Failed to fetch news")
        }
    }

    suspend fun getTopHeadlines(country: String): List<NewsArticle> {
        val newsResponse = newsApiService.getTopHeadlines(country)
        if (newsResponse.isSuccessful && newsResponse.body() != null) {
            val responseBody = newsResponse.body() ?: throw Exception("Empty response body")
            val articleList = responseBody.articles
            return newsResponseDtoMapToNewsArticle(articleList)
        } else {
            throw Exception("Failed to fetch news")
        }
    }
}