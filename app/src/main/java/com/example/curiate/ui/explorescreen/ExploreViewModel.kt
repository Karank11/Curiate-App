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

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getNewsBySearch(query: String) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _errorMessage.value = "Check Internet Connection"
            _isLoading.value = false
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val articles = newsRepository.getNewsByQuery(query)
                _newsArticles.value = articles
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTopHeadlines() {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _errorMessage.value = "Check Internet Connection"
            _isLoading.value = false
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val articles = newsRepository.getTopHeadlines("us")
                _newsArticles.value = articles
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}