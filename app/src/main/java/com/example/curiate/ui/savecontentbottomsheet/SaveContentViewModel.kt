package com.example.curiate.ui.savecontentbottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curiate.data.database.savedcontent.SavedContentDao
import com.example.curiate.data.database.savedcontent.SavedContentEntity
import com.example.curiate.ui.savecontentbottomsheet.SaveContentFragment.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class SaveContentViewModel(private val savedContentDao: SavedContentDao): ViewModel() {

    private val _contentTitle = MutableLiveData<String>()
    val contentTitle: LiveData<String> = _contentTitle

    private val _contentUrl = MutableLiveData<String>()
    val contentUrl: LiveData<String> = _contentUrl

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchLinkPreview(args: Bundle){
        viewModelScope.launch {
            _isLoading.postValue(true)
            withContext(Dispatchers.IO) {
                try {
                    val text = args.getString(Intent.EXTRA_TEXT) ?: ""
                    var initialTitle = args.getString(Intent.EXTRA_SUBJECT) ?: ""
                    val sharedUrl = findUrlInText(text) ?: return@withContext
                    val resolvedUrl = resolveFinalUrl(sharedUrl)
                    if (initialTitle.isEmpty()) {
                        initialTitle = text.substringBefore(sharedUrl).trim()
                    }

                    val doc = Jsoup.connect(resolvedUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)") // fake browser
                        .referrer("http://www.google.com")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .referrer("https://www.google.com")
                        .timeout(10000) // 10 seconds
                        .followRedirects(true)
                        .get()

                    val title = doc.select("meta[property=og:title]")
                        .attr("content").ifEmpty {
                            doc.title()
                        }.ifEmpty { initialTitle }

                    var imgUrl = doc.select("meta[property=og:image]").attr("content") ?: ""
                    if (imgUrl.startsWith("http:")) {
                        imgUrl = imgUrl.replaceFirst("http:", "https:")
                    }
                    val url = doc.select("meta[property=og:url]")
                        .attr("content").ifEmpty { resolvedUrl }

                    _contentTitle.postValue(title)
                    _contentUrl.postValue(url)
                    _imageUrl.postValue(imgUrl)
                } catch (e: Exception) {
                    Log.d(TAG, "fetchLinkPreview: $e")
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    /**
     * Finds the first URL in a given block of text.
     * @param text The text to search within.
     * @return The first found URL as a String, or null if no URL is found.
     */
    private fun findUrlInText(text: String): String? {
        val matcher = Patterns.WEB_URL.matcher(text)
        return if (matcher.find()) {
            matcher.group()
        } else {
            null
        }
    }

    private suspend fun resolveFinalUrl(url: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient.Builder().followRedirects(true).build()
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Android)") // important
                    .build()
                client.newCall(request).execute().use { response ->
                    response.request.url.toString()
                }
            } catch (e: Exception) {
                Log.d(TAG, "resolveFinalUrl: $e")
                url
            }
        }
    }

    fun insertSavedPostToDatabase() {
        viewModelScope.launch {
            val savedContentEntity = SavedContentEntity(
                contentUrl = contentUrl.value ?: "",
                imageUrl = imageUrl.value ?: "",
                title = contentTitle.value ?: ""
            )
            savedContentDao.insertSavedContent(savedContentEntity)
        }
    }
}