package com.example.curiate.ui.explorescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.curiate.data.repository.NewsRepository
import com.example.curiate.domain.models.NewsArticle
import com.example.curiate.utils.NetworkUtils
import kotlinx.coroutines.launch

class ExploreViewModel(private val newsRepository: NewsRepository, application: Application): AndroidViewModel(application) {

    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> get() = _newsArticles

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getNewsBySearch(query: String) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _errorMessage.value = "Check Internet Connection"
            return
        }
        viewModelScope.launch {
            try {
                val articles = newsRepository.getNewsByQuery(query)
                _newsArticles.value = articles
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

}