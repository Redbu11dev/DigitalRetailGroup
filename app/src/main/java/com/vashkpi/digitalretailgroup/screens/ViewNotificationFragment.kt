package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentViewNotificationBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewNotificationFragment : Fragment() {

    private var _binding: FragmentViewNotificationBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}