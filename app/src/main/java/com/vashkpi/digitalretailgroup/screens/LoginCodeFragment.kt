package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.vashkpi.digitalretailgroup.utils.hideKeyboard
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginCodeFragment :
    BaseFragment<FragmentLoginCodeBinding, LoginCodeViewModel>(FragmentLoginCodeBinding::inflate) {

    override val viewModel: LoginCodeViewModel by viewModels()

    private val args: LoginCodeFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

//        val toolbar = binding.customToolbar.toolbar
//
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        val phoneStringRaw = args.phoneStringRaw
        val phoneStringFormattedFakeSpaces = args.phoneStringFormatted.replace(" ", " ")

        binding.text2.text = String.format(
            getString(R.string.login_code_description),
            phoneStringFormattedFakeSpaces
        )

        binding.txtPinEntry.doAfterTextChanged {
            if (it.toString().length > 3) {
                viewModel.confirmCode(phoneStringRaw, it.toString())
            }
            viewModel.postErrorText("")
        }

        binding.txtPinEntry.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.txtPinEntry.hideKeyboard()
            }
            //viewModel.postErrorText("")
        }

        binding.sendSmsAgain.setOnClickListener {
            viewModel.sendSmsAgain(phoneStringRaw)
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.errorText.collect {
                binding.errorText.text = it
                Timber.d("RECEIVED ID: _$it")
                if (it != "") {
                    binding.txtPinEntry.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange
                        )
                    )
                    binding.txtPinEntry.setPinBackground(ContextCompat.getDrawable(requireContext(), R.drawable.pin_background_error))
                }
                else {
                    binding.txtPinEntry.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.txtPinEntry.setPinBackground(ContextCompat.getDrawable(requireContext(), R.drawable.selector_pin_background))
                }
            }
        }
    }

}