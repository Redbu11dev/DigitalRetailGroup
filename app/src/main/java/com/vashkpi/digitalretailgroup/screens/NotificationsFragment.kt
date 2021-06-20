package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.util.Attributes
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.NotificationsAdapter
import com.vashkpi.digitalretailgroup.adapters.helpers.SwipeToDeleteCallback
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>(FragmentNotificationsBinding::inflate) {

    override val viewModel: NotificationsViewModel by viewModels()

    lateinit var adapter: NotificationsAdapter

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        adapter = NotificationsAdapter { view, data ->
            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment())
        }

        binding.notificationsList.adapter = adapter
        (binding.notificationsList.adapter as NotificationsAdapter).mode = Attributes.Mode.Single

        viewModel.obtainNotifications()
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notificationsList.collect {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.emptyContainerVisible.collect {
                if (it) {
                    binding.emptyContainer.visibility = View.VISIBLE
                }
                else {
                    binding.emptyContainer.visibility = View.GONE
                }
            }
        }
    }

}