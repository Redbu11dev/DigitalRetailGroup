package com.vashkpi.digitalretailgroup.screens.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.vashkpi.digitalretailgroup.MainActivity
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Used to make inflation work in onCreateView
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Base fragment
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(private val inflate: Inflate<VB>) : Fragment(), LifecycleObserver {

    protected open var bottomNavigationViewVisibility = View.GONE
    protected open var orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    protected var progressView: View? = null //progress view should be included inside layout xml

    protected abstract val viewModel: BaseViewModel

    private var _binding: VB? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.root.findViewWithTag<ViewGroup>("progressViewRoot")?.let {
            progressView = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        //observeViews()
        observeViewModel()
    }

    /**
     * Set up all the views
     */
    open fun setUpViews() {
        //dummy
    }

//    open fun observeViews() {
//        //dummy
//    }

    /**
     * Observe view model
     */
    open fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.d("collecting navigation event: $it")
                    findNavController().safeNavigate(it)
                    //findNavController().navigate(it)
                }
            }
        }

        progressView?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.progressViewVisible.collect {
                        Timber.d("changing progress view visibility: $it")
                        setProgressViewEnabled(it)
                    }
                }
            }
        }
    }

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
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            mainActivity.requestedOrientation = orientation
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun BaseFragment<VB, VM>.setProgressViewEnabled(enabled: Boolean) {
        this@BaseFragment.progressView?.apply { visibility = if (enabled) View.VISIBLE else View.GONE }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.navigationEvent.collect {
//                    Timber.i("collecting navigation event ${it}")
//                    findNavController().safeNavigate(it)
//                }
//            }
//        }
//    }

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

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//    }

}