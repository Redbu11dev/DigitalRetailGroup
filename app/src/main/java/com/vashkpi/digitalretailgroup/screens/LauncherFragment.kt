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
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LauncherFragment : BaseFragment<FragmentLauncherBinding, LauncherViewModel>(FragmentLauncherBinding::inflate) {

    override val viewModel: LauncherViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIfHasToken()
    }

}