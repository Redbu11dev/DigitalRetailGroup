package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.BrandsAdapter
import com.vashkpi.digitalretailgroup.adapters.MainOptionsAdapter
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentMainBinding
import com.vashkpi.digitalretailgroup.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {

    override var _bottomNavigationViewVisibility = View.VISIBLE

    override val viewModel: MainViewModel by viewModels()

    private val adapter = BrandsAdapter { view, data ->
        viewModel.onBrandsListItemClick(data)
    }

    private val optionsAdapter = MainOptionsAdapter { view, data ->
        when (data) {
            getString(R.string.main_how_to_save_points) -> {
                viewModel.onSavePointsOptionClick()
            }
            getString(R.string.main_how_to_spend_points) -> {
                viewModel.onSpendPointsOptionClick()
            }
            getString(R.string.main_ask_question) -> {

            }
            getString(R.string.main_privacy_policy) -> {

            }
            else -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("I am recreated")

        lifecycleScope.launchWhenCreated {
            viewModel.brandsList.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun setupToolbar() {
        getCustomToolbarBinding().setUpWithNavController(
            titleText = null,
            navController = findNavController()
        ).showLogo(
            showLogo = true
        ).setButtons(
            buttonIcons = arrayOf(
                R.drawable.ic_bell,
                R.drawable.ic_profile
            )
        ) { id ->
            when (id) {
                R.id.button0 -> {
                    viewModel.onMenuNotificationsItemClick()
                }
                R.id.button1 -> {
                    viewModel.onMenuProfileItemClick()
                }
            }
        }
    }

    override fun setUpViews() {
        super.setUpViews()



//        val toolbar = binding.customToolbar.toolbar
//
//        toolbar.inflateMenu(R.menu.toolbar_main_menu)
//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.notifications -> {
//                    viewModel.onMenuNotificationsItemClick()
//                    true
//                }
//                R.id.profile -> {
//                    viewModel.onMenuProfileItemClick()
//                    true
//                }
//                else -> false
//            }
//        }
//
//        binding.customToolbar.logo.visibility = View.VISIBLE
//        toolbar.title = ""

//        adapter = BrandsAdapter { view, data ->
//            viewModel.onBrandsListItemClick(data)
//        }

        binding.partnersList.adapter = adapter

        optionsAdapter.submitList(resources.getStringArray(R.array.main_options_array_names).toMutableList())
        binding.optionsList.adapter = optionsAdapter

        //binding.partnersList.itemAnimator = null
        //binding.partnersList.visibility = View.GONE

        //viewModel.obtainBrands()
    }

    override fun onResume() {
        super.onResume()

        //viewModel.obtainBrands()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        lifecycleScope.launchWhenResumed {
            viewModel.brandsListLoading.collect {
                if (it) {
                   binding.partnersListProgress.visibility = View.VISIBLE
                }
                else {
                    binding.partnersListProgress.visibility = View.GONE
                }
            }
        }

    }

}