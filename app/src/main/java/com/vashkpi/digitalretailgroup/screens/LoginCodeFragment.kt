package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginCodeBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginCodeFragment : BaseFragment<FragmentLoginCodeBinding, LoginCodeViewModel>(FragmentLoginCodeBinding::inflate) {

    override val viewModel: LoginCodeViewModel by viewModels()

    private val args: LoginCodeFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar

        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        val phoneStringRaw = args.phoneStringRaw
        val phoneStringFormattedFakeSpaces = args.phoneStringFormatted.replace(" ", " ")

        binding.text2.text = String.format(getString(R.string.login_code_description), phoneStringFormattedFakeSpaces)

        binding.phone.apply {
            doAfterTextChanged {
                if (it.toString().length > 3) {
                    viewModel.confirmCode(phoneStringRaw, it.toString())
                }
            }
        }

    }

}