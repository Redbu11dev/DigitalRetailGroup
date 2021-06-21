package com.vashkpi.digitalretailgroup.screens

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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {

    override var _bottomNavigationViewVisibility = View.VISIBLE

    override val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: BrandsAdapter

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

        adapter = BrandsAdapter { view, data ->
            viewModel.onBrandsListItemClick(data)
        }

        binding.partnersList.adapter = adapter

        binding.item1.root.changeAlphaOnTouch()
        binding.item1.root.setOnClickListener {
            viewModel.getSavePointsRules()
        }
        binding.item1.icon.setImageResource(R.drawable.ic_save)
        binding.item1.text.setText(R.string.main_how_to_save_points)

        binding.item2.root.changeAlphaOnTouch()
        binding.item2.root.setOnClickListener {
            viewModel.getSpendPointsRules()
        }
        binding.item2.icon.setImageResource(R.drawable.ic_spend)
        binding.item2.text.setText(R.string.main_how_to_spend_points)

        viewModel.obtainBrands()
    }

    override fun onResume() {
        super.onResume()

        //viewModel.obtainBrands()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.brandsList.collect {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

}