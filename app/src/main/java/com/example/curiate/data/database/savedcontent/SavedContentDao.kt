package com.example.curiate.data.database.savedcontent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedContentDao {
    @Query("SELECT * FROM saved_content_table")
    suspend fun getAllSavedContent(): List<SavedContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedContent(savedContent: SavedContentEntity)

    @Delete
    suspend fun deleteSavedContent(savedContent: SavedContentEntity)
}