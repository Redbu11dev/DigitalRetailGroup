package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_blank, container, false)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val toolbar = binding.customToolbar.toolbar
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_barcode,
//            R.id.navigation_main  // set all your top level destinations in here
//        )
//        )
//
//        val navHostFragment = NavHostFragment.findNavController(this)
//        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)
//
//        setHasOptionsMenu(true)
//
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        toolbar.setNavigationOnClickListener { view ->
//            view.findNavController().navigateUp()
//        }
//
//        toolbar.setNavigationIcon(R.drawable.ic_bell)
//        toolbar.navigationContentDescription = "asdsadad"
//    }
}