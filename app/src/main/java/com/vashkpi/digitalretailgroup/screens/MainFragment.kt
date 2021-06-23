package com.vashkpi.digitalretailgroup.screens

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.BrandsAdapter
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentMainBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {

    override var _bottomNavigationViewVisibility = View.VISIBLE

    override val viewModel: MainViewModel by viewModels()

    private val adapter = BrandsAdapter { view, data ->
        viewModel.onBrandsListItemClick(data)
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

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar

        toolbar.inflateMenu(R.menu.toolbar_main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notifications -> {
                    viewModel.onMenuNotificationsItemClick()
                    true
                }
                R.id.profile -> {
                    viewModel.onMenuProfileItemClick()
                    true
                }
                else -> false
            }
        }

        binding.customToolbar.logo.visibility = View.VISIBLE
        toolbar.title = ""

//        adapter = BrandsAdapter { view, data ->
//            viewModel.onBrandsListItemClick(data)
//        }

        binding.partnersList.adapter = adapter
        //binding.partnersList.itemAnimator = null
        //binding.partnersList.visibility = View.GONE

        binding.item1.root.changeAlphaOnTouch()
        binding.item1.root.setOnClickListener {
            viewModel.onSavePointsOptionClick()
        }
        binding.item1.icon.setImageResource(R.drawable.ic_save)
        binding.item1.text.setText(R.string.main_how_to_save_points)

        binding.item2.root.changeAlphaOnTouch()
        binding.item2.root.setOnClickListener {
            viewModel.onSpendPointsOptionClick()
        }
        binding.item2.icon.setImageResource(R.drawable.ic_spend)
        binding.item2.text.setText(R.string.main_how_to_spend_points)


        //viewModel.obtainBrands()
    }

    override fun onResume() {
        super.onResume()

        //viewModel.obtainBrands()
    }

    override fun observeViewModel() {
        super.observeViewModel()

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.brandsList.collect {
//                adapter.submitList(it)
////                adapter.setList(it)
////                adapter.notifyDataSetChanged()
//                //binding.partnersList.scheduleLayoutAnimation()
//                binding.partnersList.visibility = View.VISIBLE
//            }
//        }
    }

}