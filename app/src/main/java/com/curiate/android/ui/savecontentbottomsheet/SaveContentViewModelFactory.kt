package com.curiate.android.ui.savecontentbottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.curiate.android.data.database.savedcontent.SavedContentDao

class SaveContentViewModelFactory(private val savedContentDao: SavedContentDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveContentViewModel::class.java)) {
            return SaveContentViewModel(savedContentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
