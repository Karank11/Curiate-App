package com.example.curiate.domain.models

data class NewsData (
    val id: Int,
    val imageUrl: String,
    val sourceName: String,
    val publishedAt: String,
    val title: String,
    val content: String,
    val author: String,
)