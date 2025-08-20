package com.example.curiate.domain.models

import com.example.curiate.data.network.ArticleDto

data class NewsArticle (
    val imageUrl: String,
    val sourceName: String,
    val publishedAt: String,
    val title: String,
    val content: String,
    val author: String,
)

fun newsResponseDtoMapToNewsArticle(articleList: List<ArticleDto>) = articleList.map { articleDto ->
    NewsArticle(
        imageUrl = articleDto.urlToImage,
        sourceName = articleDto.source.name,
        publishedAt = articleDto.publishedAt,
        title = articleDto.title,
        content = articleDto.content,
        author = articleDto.author
    )
}