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
import com.vashkpi.digitalretailgroup.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BrandInfoFragment : BaseFragment<FragmentBrandInfoBinding, BrandInfoViewModel>(FragmentBrandInfoBinding::inflate) {

    override val viewModel: BrandInfoViewModel by viewModels()

    private val args: BrandInfoFragmentArgs by navArgs()

    private val adapter = BrandInfoAdapter { view, brandInfoRegion ->
        findNavController().navigate(BrandInfoFragmentDirections.actionBrandInfoFragmentToStoresFragment(args.brand, brandInfoRegion))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getBrandInfo(args.brand)
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

//        adapter = BrandInfoAdapter { view, brandInfoRegion ->
//            findNavController().navigate(BrandInfoFragmentDirections.actionBrandInfoFragmentToStoresFragment(brand, brandInfoRegion))
//        }

        binding.infoList.adapter = adapter

        binding.inclItemBrandInfoWebsite.root.changeAlphaOnTouch()
        binding.inclItemBrandInfoWebsite.root.setOnClickListener {
            safeOpenUrlInBrowser(binding.inclItemBrandInfoWebsite.text.text.toString())
        }

        binding.inclItemBrandInfoContact.root.changeAlphaOnTouch()
        binding.inclItemBrandInfoContact.root.setOnClickListener {
            safeOpenCallDialer(binding.inclItemBrandInfoContact.phone.text.toString())
        }

//        viewModel.getBrandInfo(brand)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.brandInfo.collect {
                it?.let {

//                    binding.customToolbar.toolbar.title = it.name
                    getCustomToolbarBinding().setTitle(it.name)

                    val website = it.website
                    //val website = "www.mi.com/ru"
                    binding.inclItemBrandInfoWebsite.text.text = website
                    if (website.isBlank()) {
                        binding.inclItemBrandInfoWebsite.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemBrandInfoWebsite.root.visibility = View.VISIBLE
                    }

                    val telephone = it.telephone
                    //val telephone = "8-800-775-66-15"
                    binding.inclItemBrandInfoContact.phone.text = telephone
                    if (telephone.isBlank()) {
                        binding.inclItemBrandInfoContact.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemBrandInfoContact.root.visibility = View.VISIBLE
                    }

                    val timeOfWork = it.time_of_work
                    //val timeOfWork = "с 9:00 до 20:00 МСК, Пн – Пт"
                    binding.inclItemBrandInfoContact.workHours.text = timeOfWork
                    if (timeOfWork.isBlank()) {
                        binding.inclItemBrandInfoContact.workHours.visibility = View.GONE
                    }
                    else {
                        binding.inclItemBrandInfoContact.workHours.visibility = View.VISIBLE
                    }

                    adapter.submitList(it.regions.toMutableList())
                }
            }
        }

    }

}