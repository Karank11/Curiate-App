package com.example.curiate.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.curiate.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SaveContentFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeButton: ImageButton = view.findViewById(R.id.close_button)

        closeButton.setOnClickListener {
            dismiss()
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    companion object {
        const val TAG = "SaveContentFragment"
        fun newInstance(url: String): SaveContentFragment {
            val args = Bundle()
            args.putString("url", url)
            val fragment = SaveContentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
