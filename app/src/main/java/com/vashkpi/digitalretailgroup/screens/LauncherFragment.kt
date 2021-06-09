package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentLauncherBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LauncherFragment : BaseFragment() {

    private val viewModel: LauncherViewModel by viewModels()

    private var _binding: FragmentLauncherBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.i("collecting navigation event ${it}")
                    findNavController().navigate(it)
                }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        viewModel.checkIfHasToken()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}