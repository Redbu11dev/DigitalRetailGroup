package com.vashkpi.digitalretailgroup.screens.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.databinding.DialogDatePickerBinding
import com.vashkpi.digitalretailgroup.screens.base.DialogFragmentBase

class DatePickerDialogFragment : DialogFragmentBase() {

    companion object {
        const val REQUEST_KEY = "DATE_PICKER_DIALOG_REQUEST_KEY"
        const val RESULT_PICKED = "RESULT_PICKED"
        const val PICKED_DATE_LONG = "PICKED_DATE"
    }

    //private val args: SaveProfileDataDialogFragmentArgs by navArgs()

    private var _binding: DialogDatePickerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun onStart() {
//        //super.onStart()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDatePickerBinding.inflate(inflater, container, false)
        val view = binding.root

        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(
                REQUEST_KEY to RESULT_PICKED,
                PICKED_DATE_LONG to it
            ))
            dismiss()
        }

        picker.addOnDismissListener {
            findNavController().navigateUp()
            picker.removeOnDismissListener(this)
        }

        picker.show(parentFragmentManager, picker.toString())

//        binding.btnNegative.setOnClickListener {
//            //dismiss()
//            findNavController().navigateUp()
//            setFragmentResult(REQUEST_KEY, bundleOf(REQUEST_KEY to RESULT_DO_NOT_SAVE))
//        }
//
//        binding.btnPositive.setOnClickListener {
//            //dismiss()
//            findNavController().navigateUp()
//            setFragmentResult(REQUEST_KEY, bundleOf(REQUEST_KEY to RESULT_SAVE))
//        }
//
//        binding.crossBtn.setOnClickListener {
//            //dismiss()
//            findNavController().navigateUp()
//        }

        return view
    }
}