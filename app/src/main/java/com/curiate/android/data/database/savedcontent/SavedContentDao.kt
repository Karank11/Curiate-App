package com.curiate.android.data.database.savedcontent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedContentDao {
    @Query("SELECT * FROM saved_content_table ORDER BY id DESC")
    suspend fun getAllSavedContentLatest(): List<SavedContentEntity>

    @Query("SELECT * FROM saved_content_table ORDER BY id ASC")
    suspend fun getAllSavedContentOldest(): List<SavedContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedContent(savedContent: SavedContentEntity)

    @Delete
    suspend fun deleteSavedContent(savedContent: SavedContentEntity)

    @Query("SELECT DISTINCT category FROM saved_content_table")
    suspend fun getAllCategories(): List<String>

    // get saved content by category
    @Query("SELECT * FROM saved_content_table WHERE category = :category ORDER BY id DESC")
    suspend fun getSavedContentByCategoryLatest(category: String): List<SavedContentEntity>

    @Query("SELECT * FROM saved_content_table WHERE category = :category ORDER BY id ASC")
    suspend fun getSavedContentByCategoryOldest(category: String): List<SavedContentEntity>


}