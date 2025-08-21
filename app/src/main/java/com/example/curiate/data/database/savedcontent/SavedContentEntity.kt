package com.example.curiate.data.database.savedcontent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_content_table")
data class SavedContentEntity (
    @PrimaryKey
    val contentUrl: String,
    val imageUrl: String,
    val title: String
)