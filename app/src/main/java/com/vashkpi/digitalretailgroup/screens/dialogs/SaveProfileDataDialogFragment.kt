package com.vashkpi.digitalretailgroup.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vashkpi.digitalretailgroup.databinding.DialogSaveProfileDataBinding
import com.vashkpi.digitalretailgroup.screens.base.DialogFragmentBase

class SaveProfileDataDialogFragment : DialogFragmentBase() {

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

        binding.crossBtn.setOnClickListener {
            dismiss()
        }

        binding.btnNegative.setOnClickListener {
            dismiss()
        }

        binding.btnPositive.setOnClickListener {
            dismiss()
        }

        return view
    }
}