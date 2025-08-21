package com.example.curiate.ui.savedscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curiate.data.database.savedcontent.SavedContentDao

class SavedScreenViewModelFactory(
    private val database: SavedContentDao,
    private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedScreenViewModel::class.java)) {
            return SavedScreenViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}