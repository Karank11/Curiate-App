package com.example.curiate.data.database.savedcontent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_content_table")
data class SavedContentEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("content_url")
    val contentUrl: String,

    @ColumnInfo("image_url")
    val imageUrl: String,

    @ColumnInfo("title")
    val title: String
)