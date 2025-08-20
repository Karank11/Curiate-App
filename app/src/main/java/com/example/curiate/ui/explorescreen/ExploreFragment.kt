package com.example.curiate.ui.explorescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.explore_recycler_view)
        val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE
        val exploreAdapter = ExploreListAdapter()
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
}