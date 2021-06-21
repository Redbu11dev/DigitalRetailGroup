package com.vashkpi.digitalretailgroup.screens

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentStoreInfoBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreInfoFragment : BaseFragment<FragmentStoreInfoBinding, StoreInfoViewModel>(FragmentStoreInfoBinding::inflate) {

    override val viewModel: StoreInfoViewModel by viewModels()

    private val args: StoreInfoFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        viewModel.obtainStoreInfo(args.storeId)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.storeInfo.collect {
                it?.let {

                    Glide
                        .with(binding.image)
                        .load(it.image_parth)
                        .placeholder(R.drawable.img_dummy_store_image)
                        .into(binding.image)

                    binding.description.text = it.description
                }
            }
        }
    }
}