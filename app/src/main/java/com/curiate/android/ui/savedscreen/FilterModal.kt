package com.curiate.android.ui.savedscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.curiate.android.R
import com.curiate.android.data.database.CuriateDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class FilterModal : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filterOptions: AutoCompleteTextView = view.findViewById(R.id.filter_options)
        val cancelButton: Button = view.findViewById(R.id.cancel_button)
        val submitButton: Button = view.findViewById(R.id.submit_button)

        val database = CuriateDatabase.getInstance(requireContext()).savedContentDao
        val viewModel: SavedScreenViewModel by activityViewModels {
            SavedScreenViewModelFactory(database, requireActivity().application)
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf<String>())
        filterOptions.setAdapter(adapter)
        viewModel.categories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.clear()
                adapter.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        submitButton.setOnClickListener {
            handleSubmitAction(view, viewModel)
            dismiss()
        }
    }

    private fun handleSubmitAction(view: View, viewModel: SavedScreenViewModel) {
        /**
         * steps to do on submit button click are:
         * 1. get the selected category
         * 2. get the sort by order
         * 3. make db call to get all saved content by category
         */
        val filterDropdown: TextInputLayout = view.findViewById(R.id.filter_dropdown)
        val sortChips: ChipGroup = view.findViewById(R.id.sort_chips)
        val sortOrder = when (sortChips.checkedChipId) {
            R.id.chip_latest -> "latest"
            R.id.chip_oldest -> "oldest"
            else -> "latest"
        }
        val selectedCategory = filterDropdown.editText?.text.toString()
        viewModel.updateSavedScreen(sortOrder, selectedCategory)
    }
}
