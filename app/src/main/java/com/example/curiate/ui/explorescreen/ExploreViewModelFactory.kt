package com.example.curiate.ui.explorescreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.curiate.data.repository.NewsRepository

class ExploreViewModelFactory(private val newsRepository: NewsRepository, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(newsRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}