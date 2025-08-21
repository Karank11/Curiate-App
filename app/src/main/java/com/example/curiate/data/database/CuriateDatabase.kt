package com.example.curiate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.curiate.data.database.savedcontent.SavedContentDao
import com.example.curiate.data.database.savedcontent.SavedContentEntity

@Database(entities = [SavedContentEntity::class], version = 1, exportSchema = false)
abstract class CuriateDatabase: RoomDatabase() {
    abstract val savedContentDao: SavedContentDao

    companion object {
        @Volatile
        private var INSTANCE: CuriateDatabase? = null

        fun getInstance(context: Context): CuriateDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CuriateDatabase::class.java,
                        "curiate_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}