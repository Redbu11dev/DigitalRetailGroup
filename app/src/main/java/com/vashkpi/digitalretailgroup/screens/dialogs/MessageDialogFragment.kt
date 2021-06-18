package com.vashkpi.digitalretailgroup.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vashkpi.digitalretailgroup.databinding.DialogMessageBinding
import com.vashkpi.digitalretailgroup.databinding.DialogSaveProfileDataBinding
import com.vashkpi.digitalretailgroup.screens.ProfileFragmentArgs
import com.vashkpi.digitalretailgroup.screens.base.DialogFragmentBase

class MessageDialogFragment : DialogFragmentBase() {

    private val args: MessageDialogFragmentArgs by navArgs()

    private var _binding: DialogMessageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMessageBinding.inflate(inflater, container, false)
        val view = binding.root

        when (args.title) {
            0 -> {
                binding.title.visibility = View.GONE
            }
            else -> {
                binding.title.setText(args.title)
            }
        }

        binding.message.text = args.message

        binding.crossBtn.setOnClickListener {
            //dismiss()
            findNavController().navigateUp()
        }

        binding.btnNegative.setOnClickListener {
            //dismiss()
            findNavController().navigateUp()
        }

        return view
    }
}