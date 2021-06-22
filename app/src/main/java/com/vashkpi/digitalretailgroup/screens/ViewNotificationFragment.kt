package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentViewNotificationBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ViewNotificationFragment : BaseFragment<FragmentViewNotificationBinding, ViewNotificationViewModel>(FragmentViewNotificationBinding::inflate) {

    override val viewModel: ViewNotificationViewModel by viewModels()

    private val args: ViewNotificationFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        toolbar.inflateMenu(R.menu.toolbar_menu_notifications)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    //viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
                    true
                }
                else -> false
            }
        }

        val notification = args.notification
        val page = args.page

        binding.title.text = notification.title
        binding.text.text = notification.text
        binding.date.text = notification.text

        viewModel.markRead(notification)

    }

    override fun observeViewModel() {
        super.observeViewModel()

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.notification.collect {
//                it?.let {
//                    binding.title.text = it.title
//                    binding.text.text = it.text
//                    binding.date.text = it.text
//                }
//            }
//        }

    }

}