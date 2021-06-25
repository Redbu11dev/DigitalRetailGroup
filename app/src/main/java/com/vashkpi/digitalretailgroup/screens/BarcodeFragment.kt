package com.vashkpi.digitalretailgroup.screens

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

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
            viewModel.onPromotionRulesClick()
        }

        binding.howToGetPointsBtn.setOnClickListener {
            //throw RuntimeException("Test Crash"); // Force a crash
            viewModel.onHowToGetPointsBtnClick()
        }

        binding.newCodeBtn.setOnClickListener {
            viewModel.getNewCode()
        }

        viewModel.getBalance()

        //displayBitmap(binding.barcodeImage, it)

    }

    override fun onStart() {
        super.onStart()

        viewModel.logOpenedPurseEvent()
    }

    private fun displayBitmap(imageView: ImageView, value: String) {
        val widthPixels = (resources.displayMetrics.widthPixels * 0.9f).toInt()
        val heightPixels = (widthPixels * 0.4f).toInt()

        imageView.setImageBitmap(
            createBarcodeBitmap(
                barcodeValue = value,
                barcodeColor = getColor(requireContext(), R.color.one_layer_end),
                backgroundColor = getColor(requireContext(), R.color.white),
                widthPixels = widthPixels,
                heightPixels = heightPixels
            )
        )
    }

    private fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.barcodeValue.collect {
                displayBitmap(binding.barcodeImage, it)
            }
        }

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

        val outputDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.countDown.collect {
                binding.codeTimerTime.text = outputDateFormat.format(it)

//                val minutes = TimeUnit.MILLISECONDS.toMinutes(it)
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(it)-minutes
//
//                binding.codeTimerTime.text = "$minutes:$seconds"
            }
        }


    }
}