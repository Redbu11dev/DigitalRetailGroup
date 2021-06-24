package com.vashkpi.digitalretailgroup

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DigitalRetailGroup)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //val toolbar = binding.customToolbar.toolbar

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_barcode, R.id.navigation_main
//            )
//        )
        //setupActionBarWithNavController(navController, appBarConfiguration)

        //toolbar.setupWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

//        R.animator.nav_default_pop_enter_anim
//        R.animator.nav_default_pop_exit_anim
    }

    /**
     * This is crucial to support ActionBar's/toolbar's back button
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        // get the reference of the bottomNavigationView and set the visibility.
        binding.navView.visibility = visibility
    }
}