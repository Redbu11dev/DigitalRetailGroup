package com.vashkpi.digitalretailgroup.screens.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.vashkpi.digitalretailgroup.MainActivity

/**
 * Base fragment, containing commonly used methods
 */
abstract class BaseFragment : Fragment(), LifecycleObserver {

    protected open var bottomNavigationViewVisibility = View.GONE
    protected open var orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
//        if (activity is MainActivity) {
//            var  mainActivity = activity as MainActivity
//            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        if (activity is MainActivity) {
            var  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            mainActivity.requestedOrientation = orientation
        }
        activity?.lifecycle?.removeObserver(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            var  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            mainActivity.requestedOrientation = orientation
        }
    }

//    @SuppressLint("RestrictedApi")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (requireActivity() as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)
//        if (showActionBar) {
//            (requireActivity() as AppCompatActivity).supportActionBar?.show()
//        }
//        else {
//            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        }
//    }

    lateinit var progressView: View

    fun BaseFragment.setProgressViewEnabled(enabled: Boolean) {
        this@BaseFragment.progressView.apply { visibility = if (enabled) View.VISIBLE else View.GONE }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//    }

}