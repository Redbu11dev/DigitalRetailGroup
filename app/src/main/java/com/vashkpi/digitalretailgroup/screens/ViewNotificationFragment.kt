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
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ViewNotificationFragment : BaseFragment<FragmentViewNotificationBinding, ViewNotificationViewModel>(FragmentViewNotificationBinding::inflate) {

    override val viewModel: ViewNotificationViewModel by viewModels()

    private val args: ViewNotificationFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        val notification = args.notification

        binding.title.text = notification.title
        binding.text.text = notification.text
        binding.date.text = notification.date

        toolbar.inflateMenu(R.menu.toolbar_menu_notifications)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    //viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
                    viewModel.delete(notification.notification_id)
                    true
                }
                else -> false
            }
        }

        viewModel.markRead(notification)

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notificationRemovedEvent.collect {
                findNavController().navigateUp()
            }
        }

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