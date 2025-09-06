package com.curiate.android.ui.savedscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.curiate.android.R
import com.curiate.android.data.database.CuriateDatabase


class SavedScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_saved_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.saved_content_recycler_view)
        val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        val emptyGroup: Group = view.findViewById(R.id.empty_group)
        val postsGroup: Group = view.findViewById(R.id.posts_group)

        toolbar.title = "Saved Posts"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.saved_screen_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.filter_action_menu) {
                    val filterModal = FilterModal()
                    filterModal.show(childFragmentManager, "FilterModal")
                    true
                } else {
                    false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        progressBar.visibility = View.VISIBLE

        val database = CuriateDatabase.getInstance(requireContext()).savedContentDao
        val viewModel: SavedScreenViewModel by activityViewModels {
            SavedScreenViewModelFactory(database, requireActivity().application)
        }

        val adapter = SavedContentListAdapter { contentUrl ->
            onPostClick(contentUrl)
        }
        recyclerView.adapter = adapter

        viewModel.savedPosts.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                emptyGroup.visibility = View.VISIBLE
                postsGroup.visibility = View.GONE
            } else {
                emptyGroup.visibility = View.GONE
                postsGroup.visibility = View.VISIBLE
                adapter.submitList(list)
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