package com.curiate.android.ui.savedscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.curiate.android.data.database.savedcontent.SavedContentDao
import com.curiate.android.data.database.savedcontent.SavedContentEntity
import com.curiate.android.domain.models.SavedContentData
import kotlinx.coroutines.launch

class SavedScreenViewModel(private val database: SavedContentDao, private val application: Application) : AndroidViewModel(application) {

    private val _savedPosts = MutableLiveData<List<SavedContentData>>()
    val savedPosts: LiveData<List<SavedContentData>> get() = _savedPosts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories

    init {
        getAllCategories()
    }

    fun getSavedPostsFromDatabase() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val savedContentEntityList = database.getAllSavedContentLatest()
            val savedContentDataList = savedContentEntityList.map { entity ->
                SavedContentData(
                    imageUrl = entity.imageUrl,
                    title = entity.title,
                    contentUrl = entity.contentUrl,
                    category = entity.category
                )
            }
            _savedPosts.postValue(savedContentDataList)
            _isLoading.postValue(false)
            getAllCategories()
        }
    }

    fun insertSavedPost(savedContentData: SavedContentData) {
        viewModelScope.launch {
            val savedContentEntity = SavedContentEntity(
                imageUrl = savedContentData.imageUrl,
                title = savedContentData.title,
                contentUrl = savedContentData.contentUrl,
                category = savedContentData.category
            )
            database.insertSavedContent(savedContentEntity)
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            val categories = database.getAllCategories()
            _categories.postValue(categories)
        }
    }

    fun updateSavedScreen(order: String, category: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _savedPosts.postValue(emptyList())
            val savedContentEntityList = if(order.equals("latest", ignoreCase = true)) {
                if (category.equals("All", ignoreCase = true)) {
                    database.getAllSavedContentLatest()
                } else {
                    database.getSavedContentByCategoryLatest(category)
                }
            } else {
                if (category.equals("All", ignoreCase = true)) {
                    database.getAllSavedContentOldest()
                } else {
                    database.getSavedContentByCategoryOldest(category)
                }
            }
            val savedContentDataList = savedContentEntityList.map { entity ->
                SavedContentData(
                    imageUrl = entity.imageUrl,
                    title = entity.title,
                    contentUrl = entity.contentUrl,
                    category = entity.category
                )
            }
            _savedPosts.postValue(savedContentDataList)
            _isLoading.postValue(false)
            getAllCategories()
        }
    }
}