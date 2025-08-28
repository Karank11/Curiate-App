package com.example.curiate.ui.savedscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.curiate.R
import com.example.curiate.data.database.CuriateDatabase


class SavedScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_saved_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.saved_content_recycler_view)
        val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        val database = CuriateDatabase.getInstance(requireContext()).savedContentDao
        val viewModel: SavedScreenViewModel by viewModels {
            SavedScreenViewModelFactory(database, requireActivity().application)
        }

        val adapter = SavedContentListAdapter { contentUrl ->
            onPostClick(contentUrl)
        }

        recyclerView.adapter = adapter

        viewModel.savedPosts.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        if (viewModel.savedPosts.value == null) {
            viewModel.getSavedPostsFromDatabase()
        }
    }

    private fun onPostClick(url: String) {
        if (url.isBlank()) {
            Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val colorPrimaryDark = ContextCompat.getColor(requireContext(), R.color.primary_dark)

            val tabsIntent = CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(CustomTabColorSchemeParams.Builder().setToolbarColor(colorPrimaryDark).build())
                .build()
            tabsIntent.launchUrl(requireContext(), Uri.parse(url))
        } catch (e: Exception) {
            // If Chrome Custom Tabs fails, fall back to the standard intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}