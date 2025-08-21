package com.example.curiate.ui.savedscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.curiate.data.database.savedcontent.SavedContentDao
import com.example.curiate.data.database.savedcontent.SavedContentEntity
import com.example.curiate.domain.models.SavedContentData
import kotlinx.coroutines.launch

class SavedScreenViewModel(private val database: SavedContentDao, private val application: Application) : AndroidViewModel(application) {

    private val _savedPosts = MutableLiveData<List<SavedContentData>>()
    val savedPosts: LiveData<List<SavedContentData>> get() = _savedPosts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getSavedPostsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            val savedContentEntityList = database.getAllSavedContent()
            val savedContentDataList = savedContentEntityList.map { entity ->
                SavedContentData(
                    imageUrl = entity.imageUrl,
                    title = entity.title,
                    contentUrl = entity.contentUrl
                )
            }
            _savedPosts.value = savedContentDataList
            _isLoading.value = false
        }
    }

    fun insertSavedPost(savedContentData: SavedContentData) {
        viewModelScope.launch {
            val savedContentEntity = SavedContentEntity(
                imageUrl = savedContentData.imageUrl,
                title = savedContentData.title,
                contentUrl = savedContentData.contentUrl
            )
            database.insertSavedContent(savedContentEntity)
        }
    }
}