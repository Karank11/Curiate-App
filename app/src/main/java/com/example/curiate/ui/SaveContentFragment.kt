package com.example.curiate.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.curiate.R
import com.example.curiate.data.database.CuriateDatabase
import com.example.curiate.data.database.savedcontent.SavedContentEntity
import com.example.curiate.domain.models.LinkPreviewData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class SaveContentFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "SaveContentFragment"
        fun newInstance(args: Bundle): SaveContentFragment {
            val fragment = SaveContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var contentImageView: ImageView
    private lateinit var contentTitleView: TextView
    private lateinit var contentUrlView: TextView
    private lateinit var contentGroup: Group
    private lateinit var loadingGroup: Group
    private lateinit var closeButton: ImageButton
    private lateinit var saveButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentImageView = view.findViewById(R.id.content_image)
        contentTitleView = view.findViewById(R.id.content_title)
        contentUrlView = view.findViewById(R.id.content_url)
        contentGroup = view.findViewById(R.id.content_group)
        loadingGroup = view.findViewById(R.id.loading_group)
        closeButton = view.findViewById(R.id.close_button)
        saveButton = view.findViewById(R.id.save_button)

        saveButton.isEnabled = false
        loadingGroup.visibility = View.VISIBLE
        contentGroup.visibility = View.GONE
        val args = arguments ?: return
        val text = args.getString(Intent.EXTRA_TEXT)
        var initialTitle = args.getString(Intent.EXTRA_SUBJECT)
        val sharedUrl = findUrlInText(text ?: "")
        if (sharedUrl == null) {
            dismiss()
            return
        }
        if (initialTitle.isNullOrEmpty()) {
            initialTitle = text?.substringBefore(sharedUrl)?.trim()
        }
        var postData: LinkPreviewData? = null
        lifecycleScope.launch {
            val resolvedUrl = resolveFinalUrl(sharedUrl)
            val previewData = fetchLinkPreview(resolvedUrl) ?: return@launch
            val contentTitle = previewData.title.ifEmpty { initialTitle ?: "" }
            val contentUrl = previewData.contentUrl.ifEmpty { sharedUrl }
            contentTitleView.text = contentTitle
            contentUrlView.text = contentUrl

            // if imageUrl is http then convert it to https
            var imageUrl = previewData.imageUrl
            if (imageUrl.startsWith("http:")) {
                imageUrl = imageUrl.replaceFirst("http:", "https:")
            }
            postData = LinkPreviewData(
                imageUrl = imageUrl,
                title = contentTitle,
                contentUrl = contentUrl
            )

            Glide.with(this@SaveContentFragment)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(contentImageView)

            saveButton.isEnabled = true
            saveButton.setBackgroundColor(resources.getColor(R.color.primary_dark))
            contentGroup.visibility = View.VISIBLE
            loadingGroup.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            // Handle save button click
            postData?.let {
                insertSavedPostToDatabase(it)
            }
            dismiss()
        }

        closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun insertSavedPostToDatabase(postData: LinkPreviewData) {
        lifecycleScope.launch {
            val database = CuriateDatabase.getInstance(requireContext()).savedContentDao
            withContext(Dispatchers.IO) {
                database.insertSavedContent(SavedContentEntity(postData.contentUrl, postData.imageUrl, postData.title))
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

    private suspend fun fetchLinkPreview(sharedUrl: String): LinkPreviewData? {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(sharedUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)") // fake browser
                    .referrer("http://www.google.com")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .referrer("https://www.google.com")
                    .timeout(10000) // 10 seconds
                    .followRedirects(true)
                    .get()

                val title = doc.select("meta[property=og:title]").attr("content").ifEmpty {
                    doc.title()
                } ?: ""
                val imageUrl = doc.select("meta[property=og:image]").attr("content") ?: ""
                val contentUrl = doc.select("meta[property=og:url]").attr("content") ?: ""
                LinkPreviewData(imageUrl, title, contentUrl)
            } catch (e: Exception) {
                Log.d(TAG, "fetchLinkPreview: $e")
                null
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
