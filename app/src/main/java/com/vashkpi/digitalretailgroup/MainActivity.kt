package com.vashkpi.digitalretailgroup

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vashkpi.digitalretailgroup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint





@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    fun Activity.transparentStatusAndNavigation(
        systemUiScrim: Int = Color.parseColor("#40000000") // 25% black
    ) {
        var systemUiVisibility = 0
        // Use a dark scrim by default since light status is API 23+
        var statusBarColor = systemUiScrim
        //  Use a dark scrim by default since light nav bar is API 27+
        var navigationBarColor = systemUiScrim
        val winParams = window.attributes


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            navigationBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.decorView.systemUiVisibility = systemUiVisibility
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags and
                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION).inv()
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
        }

        window.attributes = winParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DigitalRetailGroup)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //transparentStatusAndNavigation()
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

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
        //navController.

        //setupActionBarWithNavController(navController, appBarConfiguration)

        //toolbar.setupWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        navView.setOnNavigationItemSelectedListener {
            //Timber.d("navController.previousBackStackEntry.destination.id: ${navController.previousBackStackEntry?.destination?.id}")
            val options = NavOptions.Builder()
                .setPopUpTo(it.itemId, true)
                .setLaunchSingleTop(true)
                .build()
            when (it.itemId) {
                R.id.navigation_barcode -> {
                    navController.navigate(R.id.navigation_barcode, null, options)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_main -> {
                    navController.navigate(R.id.navigation_main, null, options)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    throw IllegalStateException("unknown destination")
                }
            }
        }

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