package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>(FragmentNotificationsBinding::inflate) {

    //private val viewModel: NotificationsViewModel by viewModels()
    override val viewModel: NotificationsViewModel by viewModels()

//    private var _binding: FragmentNotificationsBinding? = null
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

    override fun setUpViews() {
        super.setUpViews()

        val root: View = binding.root

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

        val adapter = NotificationsAdapter { view, data ->
            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment())
        }

        binding.notificationsList.adapter = adapter
        (binding.notificationsList.adapter as NotificationsAdapter).setMode(Attributes.Mode.Single)
        adapter.setList(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
        adapter.notifyDataSetChanged()
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val toolbar = binding.customToolbar.toolbar
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)
//
//        val adapter = NotificationsAdapter { view, data ->
//            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment())
//        }
//
//        binding.notificationsList.adapter = adapter
//        (binding.notificationsList.adapter as NotificationsAdapter).setMode(Attributes.Mode.Single)
//        adapter.setList(arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
//        adapter.notifyDataSetChanged()
//
//        return root
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}