package com.vashkpi.digitalretailgroup.screens.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.vashkpi.digitalretailgroup.MainActivity
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginCodeBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Base fragment, containing commonly used methods
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(private val inflate: Inflate<VB>) : Fragment(), LifecycleObserver {

    protected open var bottomNavigationViewVisibility = View.GONE
    protected open var orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    protected lateinit var progressView: View

    protected abstract val viewModel: ViewModel

    private var _binding: VB? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }

    open fun setUpViews() {}

    open fun observeView() {}

    open fun observeData() {}

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    fun BaseFragment<VB, VM>.setProgressViewEnabled(enabled: Boolean) {
        this@BaseFragment.progressView.apply { visibility = if (enabled) View.VISIBLE else View.GONE }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//    }

}