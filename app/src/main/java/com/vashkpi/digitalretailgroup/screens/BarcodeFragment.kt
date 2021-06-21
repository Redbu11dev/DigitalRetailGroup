package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BarcodeFragment : BaseFragment<FragmentBarcodeBinding, BarcodeViewModel>(FragmentBarcodeBinding::inflate) {

    override var _bottomNavigationViewVisibility = View.VISIBLE

    override val viewModel: BarcodeViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar

        toolbar.inflateMenu(R.menu.toolbar_main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notifications -> {
                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
                    true
                }
                R.id.profile -> {
                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToProfileFragment(false, ""))
                    true
                }
                else -> false
            }
        }

        binding.customToolbar.logo.visibility = View.VISIBLE
        toolbar.title = ""

        binding.contactUs.setOnClickListener {
            viewModel.getPromotionRules()
        }

        viewModel.getBalance()

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.balance.collect {
                binding.balanceAmount.text = it.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                when (it) {
                    BarcodeViewModel.ViewState.ZERO_BALANCE -> {
                        binding.howToGetPointsBtn.visibility = View.VISIBLE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.GONE
                    }
                    BarcodeViewModel.ViewState.NEW_CODE_AVAILABLE -> {
                        binding.howToGetPointsBtn.visibility = View.GONE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.VISIBLE

                        binding.newCodeBtn.visibility = View.VISIBLE
                        binding.newCodeTimerContainer.visibility = View.GONE
                    }
                    BarcodeViewModel.ViewState.NEW_CODE_MUST_WAIT -> {
                        binding.howToGetPointsBtn.visibility = View.GONE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.VISIBLE

                        binding.newCodeBtn.visibility = View.GONE
                        binding.newCodeTimerContainer.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}