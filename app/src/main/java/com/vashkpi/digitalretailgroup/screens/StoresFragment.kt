package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.BrandInfoAdapter
import com.vashkpi.digitalretailgroup.adapters.StoresAdapter
import com.vashkpi.digitalretailgroup.databinding.FragmentStoreInfoBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentStoresBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.setTitle
import com.vashkpi.digitalretailgroup.utils.showBackButtonIfAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoresFragment : BaseFragment<FragmentStoresBinding, StoresViewModel>(FragmentStoresBinding::inflate) {

    override val viewModel: StoresViewModel by viewModels()

    private val args: StoresFragmentArgs by navArgs()

    private val adapter = StoresAdapter { view, store ->
        findNavController().navigate(StoresFragmentDirections.actionStoresFragmentToStoreInfoFragment(store.store_id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.storesList.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun setupToolbar() {
        super.setupToolbar()
        getCustomToolbarBinding().showBackButtonIfAvailable(findNavController(), false)
    }

    override fun setUpViews() {
        super.setUpViews()

//        val toolbar = binding.customToolbar.toolbar
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        val brand = args.brand
        val brandInfoRegion = args.brandInfoRegion

        binding.infoList.adapter = adapter

//        binding.customToolbar.toolbar.title = brandInfoRegion.name
        getCustomToolbarBinding().setTitle(brandInfoRegion.name)

        viewModel.obtainRegionStores(brand.brand_id, brandInfoRegion.region_id)
    }

    override fun observeViewModel() {
        super.observeViewModel()

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.storesList.collect {
//                adapter.submitList(it)
//            }
//        }
    }
}