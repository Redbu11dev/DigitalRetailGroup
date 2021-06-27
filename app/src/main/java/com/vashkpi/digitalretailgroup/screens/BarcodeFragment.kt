package com.vashkpi.digitalretailgroup.screens

import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import com.vashkpi.digitalretailgroup.utils.BarcodeGenerator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BarcodeFragment : BaseFragment<FragmentBarcodeBinding, BarcodeViewModel>(FragmentBarcodeBinding::inflate) {

    override var _bottomNavigationViewVisibility = View.VISIBLE

    override val viewModel: BarcodeViewModel by viewModels()

    private val barcodeGenerator = BarcodeGenerator()

    override fun setUpViews() {
        super.setUpViews()

        setUpCustomToolbarWithNavController(
            buttonIcons = arrayOf(
                R.drawable.ic_bell,
                R.drawable.ic_profile
            )
        ) { id ->
            when (id) {
                R.id.button0 -> {
                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
                }
                R.id.button1 -> {
                    viewModel.postNavigationEvent(
                        BarcodeFragmentDirections.actionNavigationBarcodeToProfileFragment(
                            false,
                            ""
                        )
                    )
                }
            }
        }

//        val toolbar = binding.customToolbar.toolbar
//
//        toolbar.inflateMenu(R.menu.toolbar_main_menu)
//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.notifications -> {
//                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
//                    true
//                }
//                R.id.profile -> {
//                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToProfileFragment(false, ""))
//                    true
//                }
//                else -> false
//            }
//        }
//
//        binding.customToolbar.logo.visibility = View.VISIBLE
//        toolbar.title = ""

        binding.contactUs.setOnClickListener {
            viewModel.onPromotionRulesClick()
        }

        binding.howToGetPointsBtn.setOnClickListener {
            //throw RuntimeException("Test Crash"); // Force a crash
            viewModel.onHowToGetPointsBtnClick()
        }

        binding.newCodeBtn.setOnClickListener {
            viewModel.getNewCode()
        }

        binding.getCodeBtn.setOnClickListener {
            viewModel.getNewCode()
        }

        binding.balanceAmountError.setOnClickListener {
            viewModel.getBalance()
        }

        viewModel.getBalance()

    }

    override fun onStart() {
        super.onStart()

        viewModel.logOpenedPurseEvent()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.barcodeValue.collect {
                barcodeGenerator.displayBitmap(binding.barcodeImage, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.balance.collect {
                binding.balanceAmount.text = it.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.balanceViewState.collect {
                when (it) {
                    BarcodeViewModel.BalanceViewState.LOADING -> {
                        binding.balanceAmountError.visibility = View.INVISIBLE
                        binding.balanceAmountProgress.visibility = View.VISIBLE
                        binding.balanceAmount.visibility = View.INVISIBLE
                    }
                    BarcodeViewModel.BalanceViewState.ERROR -> {
                        binding.balanceAmountError.visibility = View.VISIBLE
                        binding.balanceAmountProgress.visibility = View.INVISIBLE
                        binding.balanceAmount.visibility = View.INVISIBLE
                    }
                    BarcodeViewModel.BalanceViewState.OBTAINED -> {
                        binding.balanceAmountError.visibility = View.INVISIBLE
                        binding.balanceAmountProgress.visibility = View.INVISIBLE
                        binding.balanceAmount.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.codeViewState.collect {
                when (it) {
                    BarcodeViewModel.CodeViewState.UNAVAILABLE -> {
                        binding.howToGetPointsBtn.visibility = View.VISIBLE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.GONE

                        binding.newCodeBtn.visibility = View.GONE
                        binding.newCodeTimerContainer.visibility = View.GONE
                    }
                    BarcodeViewModel.CodeViewState.NEW_CODE_AVAILABLE -> {
                        binding.howToGetPointsBtn.visibility = View.GONE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.VISIBLE

                        binding.newCodeBtn.visibility = View.VISIBLE
                        binding.newCodeTimerContainer.visibility = View.GONE
                    }
                    BarcodeViewModel.CodeViewState.NEW_CODE_MUST_WAIT -> {
                        binding.howToGetPointsBtn.visibility = View.GONE
                        binding.getCodeBtn.visibility = View.GONE
                        binding.codeInfoContainer.visibility = View.VISIBLE

                        binding.newCodeBtn.visibility = View.GONE
                        binding.newCodeTimerContainer.visibility = View.VISIBLE
                    }
                    BarcodeViewModel.CodeViewState.NEW_CODE_AVAILABLE_NEVER_OBTAINED -> {
                        binding.howToGetPointsBtn.visibility = View.GONE
                        binding.getCodeBtn.visibility = View.VISIBLE
                        binding.codeInfoContainer.visibility = View.GONE

                        binding.newCodeBtn.visibility = View.GONE
                        binding.newCodeTimerContainer.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.code.collect {
                binding.code.text = it.toString()
                if (it == 0) {
                    binding.code.visibility = View.GONE
                    binding.codeDescription.visibility = View.GONE
                }
                else {
                    binding.code.visibility = View.VISIBLE
                    binding.codeDescription.visibility = View.VISIBLE
                }
            }
        }

        val countDownTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.countDown.collect {
                binding.codeTimerTime.text = countDownTimeFormat.format(it)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeGenerator.dispose()
    }
}