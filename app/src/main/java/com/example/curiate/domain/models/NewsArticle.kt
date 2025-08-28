package com.example.curiate.domain.models

import com.example.curiate.data.network.ArticleDto

data class NewsArticle (
    val imageUrl: String? = "www.image.com",
    val sourceName: String? = "Source Name",
    val publishedAt: String? = "9:00",
    val title: String? = "Title",
    val content: String? = "Content",
    val author: String? = "Author Name",
    val url: String? = "https://www.google.com"
)

fun newsResponseDtoMapToNewsArticle(articleList: List<ArticleDto>) = articleList.map { articleDto ->
    NewsArticle(
        imageUrl = articleDto.urlToImage,
        sourceName = articleDto.source.name,
        publishedAt = articleDto.publishedAt,
        title = articleDto.title,
        content = articleDto.content,
        author = articleDto.author,
        url = articleDto.url
    )
}