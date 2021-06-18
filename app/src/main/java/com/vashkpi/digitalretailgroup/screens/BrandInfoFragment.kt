package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.BrandInfoAdapter
import com.vashkpi.digitalretailgroup.adapters.BrandsAdapter
import com.vashkpi.digitalretailgroup.databinding.FragmentBrandInfoBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentViewNotificationBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BrandInfoFragment : BaseFragment<FragmentBrandInfoBinding, BrandInfoViewModel>(FragmentBrandInfoBinding::inflate) {

    override val viewModel: BrandInfoViewModel by viewModels()

    private val args: BrandInfoFragmentArgs by navArgs()

    private lateinit var adapter: BrandInfoAdapter

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        val brand = args.brand

        adapter = BrandInfoAdapter { view, data ->
            findNavController().navigate(BrandInfoFragmentDirections.actionBrandInfoFragmentToStoresFragment())
        }

        binding.infoList.adapter = adapter

        viewModel.getBrandInfo(brand)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.name.collect {
                binding.customToolbar.toolbar.title = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.brandRegionsList.collect {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

}