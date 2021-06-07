package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeFragment : BaseFragment() {

    override var showActionBar = true

    private lateinit var barcodeViewModel: BarcodeViewModel
    private var _binding: FragmentBarcodeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barcodeViewModel =
            ViewModelProvider(this).get(BarcodeViewModel::class.java)

        _binding = FragmentBarcodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        barcodeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val navController = findNavController()
        binding.customToolbar.toolbar.setupWithNavController(navController)

        //(requireActivity() as AppCompatActivity).supportActionBar.menu
        setHasOptionsMenu(true)

        binding.customToolbar.toolbar.inflateMenu(R.menu.toolbar_main_menu)
        binding.customToolbar.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                // these ids should match the item ids from my_fragment_menu.xml file
                R.id.item1 -> {
                    Log.i("MyFragment", "edit menu item is clicked")
                    findNavController().navigate(R.id.action_navigation_barcode_to_notificationsFragment)

                    // by returning 'true' we're saying that the event
                    // is handled and it shouldn't be propagated further
                    true
                }
                else -> false
            }
        }

        setHasOptionsMenu(true)
        //binding.customToolbar.toolbar.setMenu()

        // if you prefer not to use xml menu file
        // you can also add menu items programmatically
//        val shareMenuItem = binding.customToolbar.toolbar.menu.add("fhdgdgd")
//        shareMenuItem.setOnMenuItemClickListener {
//            Log.i("MyFragment", "share menu item is clicked")
//            true
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val toolbar = binding.customToolbar.toolbar
//        val appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.navigation_barcode,
//            R.id.navigation_main  // set all your top level destinations in here
//            )
//        )
//
//        toolbar.setCollapseIcon(R.drawable.ic_home_black_24dp)
//        toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp)
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
//    }
//
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.toolbar_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.item1 -> {
                findNavController().navigate(R.id.action_navigation_barcode_to_notificationsFragment)
            }
            else -> {

            }
        }

        return true
    }
}