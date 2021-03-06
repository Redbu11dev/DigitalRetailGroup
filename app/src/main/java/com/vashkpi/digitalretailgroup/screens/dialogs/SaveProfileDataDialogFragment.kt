package com.vashkpi.digitalretailgroup.screens.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vashkpi.digitalretailgroup.databinding.DialogSaveProfileDataBinding
import com.vashkpi.digitalretailgroup.screens.ProfileFragmentArgs
import com.vashkpi.digitalretailgroup.screens.base.DialogFragmentBase

class SaveProfileDataDialogFragment : DialogFragmentBase() {

    companion object {
        const val REQUEST_KEY = "SAVE_PROFILE_DIALOG_REQUEST_KEY"
        const val RESULT_SAVE = "RESULT_SAVE"
        const val RESULT_DO_NOT_SAVE = "RESULT_DO_NOT_SAVE"
    }

    private val args: SaveProfileDataDialogFragmentArgs by navArgs()

    private var _binding: DialogSaveProfileDataBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSaveProfileDataBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnNegative.setOnClickListener {
            //dismiss()
            findNavController().navigateUp()
            setFragmentResult(REQUEST_KEY, bundleOf(REQUEST_KEY to RESULT_DO_NOT_SAVE))
        }

        binding.btnPositive.setOnClickListener {
            //dismiss()
            findNavController().navigateUp()
            setFragmentResult(REQUEST_KEY, bundleOf(REQUEST_KEY to RESULT_SAVE))
        }

        binding.crossBtn.setOnClickListener {
            //dismiss()
            findNavController().navigateUp()
        }

        return view
    }
}