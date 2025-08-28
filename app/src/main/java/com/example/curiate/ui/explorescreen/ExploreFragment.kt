package com.example.curiate.ui.explorescreen

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.curiate.R
import com.example.curiate.data.network.RetrofitClient
import com.example.curiate.data.repository.NewsRepository

class ExploreFragment : Fragment() {
    private val newsApiService = RetrofitClient.newsApiService
    private val newsRepository = NewsRepository(newsApiService)
    private val viewModel: ExploreViewModel by viewModels {
        ExploreViewModelFactory(newsRepository, requireActivity().application)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var exploreAdapter: ExploreListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.explore_recycler_view)
        progressBar = view.findViewById(R.id.progressbar)
        updateExploreScreen()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.explore_screen_menu, menu)
                handleSearchQuery(menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun handleSearchQuery(menu: Menu) {
        val searchItem = menu.findItem(R.id.explore_action_search)
        val searchView = searchItem?.actionView as SearchView
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        searchView.maxWidth = pixels
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.background = ContextCompat.getDrawable(requireContext(), R.drawable.search_bar_bg)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.setHintTextColor(Color.GRAY)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                query?.let {
                    viewModel.getNewsBySearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun updateExploreScreen() {
        progressBar.visibility = View.VISIBLE
        exploreAdapter = ExploreListAdapter {
            onPostClick(it)
        }
        recyclerView.adapter = exploreAdapter

        viewModel.newsArticles.observe(viewLifecycleOwner) {
            it?.let {
                exploreAdapter.submitList(it)
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
        }
        if (viewModel.newsArticles.value.isNullOrEmpty()) {
            viewModel.getTopHeadlines()
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