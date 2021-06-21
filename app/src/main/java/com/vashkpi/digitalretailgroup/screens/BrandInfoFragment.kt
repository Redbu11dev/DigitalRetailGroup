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
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch
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

        adapter = BrandInfoAdapter { view, brandInfoRegion ->
            findNavController().navigate(BrandInfoFragmentDirections.actionBrandInfoFragmentToStoresFragment(brand, brandInfoRegion))
        }

        binding.infoList.adapter = adapter

        binding.inclItemBrandInfoWebsite.root.changeAlphaOnTouch()
        binding.inclItemBrandInfoWebsite.root.setOnClickListener {

        }

        binding.inclItemBrandInfoContact.root.changeAlphaOnTouch()
        binding.inclItemBrandInfoContact.root.setOnClickListener {

        }

        viewModel.getBrandInfo(brand)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.brandInfo.collect {
                it?.let {

                    binding.customToolbar.toolbar.title = it.name

                    binding.inclItemBrandInfoWebsite.text.text = it.website

                    binding.inclItemBrandInfoContact.phone.text = it.telephone

                    binding.inclItemBrandInfoContact.workHours.text = it.time_of_work

                    adapter.setList(it.regions.toMutableList())
                    adapter.notifyDataSetChanged()
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.name.collect {
//                binding.customToolbar.toolbar.title = it
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.website.collect {
//                binding.inclItemBrandInfoWebsite.text.text = it
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.telephone.collect {
//                binding.inclItemBrandInfoContact.phone.text = it
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.timeOfWork.collect {
//                binding.inclItemBrandInfoContact.workHours.text = it
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.brandRegionsList.collect {
//                adapter.setList(it)
//                adapter.notifyDataSetChanged()
//            }
//        }
    }

}