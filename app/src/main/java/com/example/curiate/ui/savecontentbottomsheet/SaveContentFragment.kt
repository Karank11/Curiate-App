package com.example.curiate.ui.savecontentbottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.curiate.R
import com.example.curiate.data.database.CuriateDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

        val savedContentDao = CuriateDatabase.getInstance(requireActivity().applicationContext).savedContentDao
        val viewModelFactory = SaveContentViewModelFactory(savedContentDao)
        val viewModel: SaveContentViewModel by viewModels {
            viewModelFactory
        }

        val args = arguments ?: return
        viewModel.fetchLinkPreview(args)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                saveButton.isEnabled = false
                loadingGroup.visibility = View.VISIBLE
                contentGroup.visibility = View.GONE
            } else {
                saveButton.isEnabled = true
                loadingGroup.visibility = View.GONE
                contentGroup.visibility = View.VISIBLE
                saveButton.setBackgroundColor(resources.getColor(R.color.primary_dark))
            }
        }
        viewModel.contentTitle.observe(viewLifecycleOwner) {
            it?.let {
                contentTitleView.text = it
            }
        }
        viewModel.imageUrl.observe(viewLifecycleOwner) {
            it?.let {
                Glide.with(this@SaveContentFragment)
                    .load(it)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(contentImageView)

            }
        }
        viewModel.contentUrl.observe(viewLifecycleOwner) {
            it?.let {
                contentUrlView.text = it
            }
        }

        saveButton.setOnClickListener {
            viewModel.insertSavedPostToDatabase()
            dismiss()
        }
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
