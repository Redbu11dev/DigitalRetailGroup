package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentLauncherBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginCodeBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginCodeFragment : BaseFragment() {

    private val args: LoginCodeFragmentArgs by navArgs()

    private var _binding: FragmentLoginCodeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navController = findNavController()
        binding.customToolbar.toolbar.setupWithNavController(navController)

        val phoneStringFakeSpaces = args.phoneString.replace(" ", " ")
        binding.text2.text = String.format(getString(R.string.login_code_description), phoneStringFakeSpaces)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}