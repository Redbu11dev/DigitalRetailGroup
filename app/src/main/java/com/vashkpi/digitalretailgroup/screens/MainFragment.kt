package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    override var showActionBar = true

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        mainViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val toolbar = binding.customToolbar.toolbar

//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        toolbar.inflateMenu(R.menu.toolbar_main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                // these ids should match the item ids from my_fragment_menu.xml file
                R.id.item1 -> {

                    // by returning 'true' we're saying that the event
                    // is handled and it shouldn't be propagated further
                    true
                }
                else -> false
            }
        }

        binding.customToolbar.logo.visibility = View.VISIBLE
        toolbar.title = ""

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}