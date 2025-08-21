package com.example.curiate.ui.savedscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
        val adapter = SavedContentListAdapter()
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
}