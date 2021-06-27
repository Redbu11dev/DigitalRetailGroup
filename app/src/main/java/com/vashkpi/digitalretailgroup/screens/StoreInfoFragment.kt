package com.vashkpi.digitalretailgroup.screens

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentStoreInfoBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StoreInfoFragment : BaseFragment<FragmentStoreInfoBinding, StoreInfoViewModel>(FragmentStoreInfoBinding::inflate) {

    override val viewModel: StoreInfoViewModel by viewModels()

    private val args: StoreInfoFragmentArgs by navArgs()

    override fun setupToolbar() {
        super.setupToolbar()
        getCustomToolbarBinding().showBackButtonIfAvailable(findNavController(), false)
    }

    override fun setUpViews() {
        super.setUpViews()

//        val toolbar = binding.customToolbar.toolbar
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        viewModel.obtainStoreInfo(args.storeId)

        binding.inclItemWebsite.root.changeAlphaOnTouch()
        binding.inclItemWebsite.root.setOnClickListener {
            safeOpenUrlInBrowser(binding.inclItemWebsite.text.text.toString())
        }

        binding.inclItemContact.root.changeAlphaOnTouch()
        binding.inclItemContact.root.setOnClickListener {
            safeOpenCallDialer(binding.inclItemContact.phone.text.toString())
        }

        binding.inclItemInfoWorkhours.root.changeAlphaOnTouch()
        binding.inclItemInfoAddress.root.changeAlphaOnTouch()
        binding.inclItemInfoAddress.root.setOnClickListener {
//            safeOpenAddressInMaps(binding.inclItemInfoAddress.text.text.toString())
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.storeInfo.collect {
                it?.let {

//                    binding.customToolbar.toolbar.title = it.name
                    getCustomToolbarBinding().setTitle(it.name)

                    val imageView = binding.image
                    val isSvg = it.image_parth.toString().endsWith(".svg")
                    //if svg
                    if (isSvg) {
                        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(imageView)
                            .`as`(PictureDrawable::class.java)
                            //.placeholder(R.drawable.img_dummy_store_image)
                            //.transition(DrawableTransitionOptions.withCrossFade())
                            //.listener(SvgSoftwareLayerSetter())
                            .load(it.image_parth)
                            .into(imageView)
                    }
                    //if .png or .jpg or others
                    else {
                        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(imageView)
                            //.placeholder(R.drawable.img_dummy_store_image)
                            //.load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
                            .load(it.image_parth)
                            .into(imageView)
                    }

                    binding.description.text = it.description

                    binding.inclItemWebsite.text.text = it.website
                    if (it.website.isBlank()) {
                        binding.inclItemWebsite.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemWebsite.root.visibility = View.VISIBLE
                    }

                    binding.inclItemContact.workHours.visibility = View.GONE
                    binding.inclItemContact.phone.text = it.telephone
                    if (it.telephone.isBlank()) {
                        binding.inclItemContact.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemContact.root.visibility = View.VISIBLE
                    }

                    binding.inclItemInfoWorkhours.text.text = it.time_of_work
                    if (it.time_of_work.isBlank()) {
                        binding.inclItemInfoWorkhours.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemInfoWorkhours.root.visibility = View.VISIBLE
                    }

                    binding.inclItemInfoAddress.text.text = it.address
                    if (it.address.isBlank()) {
                        binding.inclItemInfoAddress.root.visibility = View.GONE
                    }
                    else {
                        binding.inclItemInfoAddress.root.visibility = View.VISIBLE
                    }

                }
            }
        }
    }
}