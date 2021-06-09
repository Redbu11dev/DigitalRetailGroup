package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginCodeBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginPhoneBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginPhoneFragment : BaseFragment() {

    private var _binding: FragmentLoginPhoneBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPhoneBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}