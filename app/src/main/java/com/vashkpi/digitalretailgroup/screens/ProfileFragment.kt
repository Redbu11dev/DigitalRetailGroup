package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navController = findNavController()
        binding.customToolbar.toolbar.setupWithNavController(navController)
        //binding.customToolbar.toolbar.setNavigationIcon(R.drawable.ic_bell)
        //binding.customToolbar.toolbar.nav

        return root
    }
}