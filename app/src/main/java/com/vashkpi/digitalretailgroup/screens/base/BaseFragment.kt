package com.vashkpi.digitalretailgroup.screens.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.*
import com.vashkpi.digitalretailgroup.MainActivity
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.CustomToolbarBinding
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import com.vashkpi.digitalretailgroup.utils.setUpWithNavController
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Used to make inflation work in onCreateView
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Base fragment
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(private val inflate: Inflate<VB>) : Fragment(), LifecycleObserver {

    protected open var _bottomNavigationViewVisibility = View.GONE
    protected open var _orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    private var _progressView: View? = null //progress view should be included inside layout xml

    protected abstract val viewModel: BaseViewModel

    private var _binding: VB? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    fun getCustomToolbarBinding(): CustomToolbarBinding? {
        val toolbarLayout = binding.root.findViewById<ViewGroup>(R.id.custom_toolbar)
        if (toolbarLayout == null) {
            return null
        }
        else {
            return CustomToolbarBinding.bind(toolbarLayout)
        }
    }

    fun getLabel(): String? {
        findNavController().currentDestination?.let {
            return it.label.toString()
        } ?: kotlin.run {
            return null
        }
    }

    open fun setupToolbar() {
        getCustomToolbarBinding().setUpWithNavController(
            titleText = getLabel(),
            navController = findNavController()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enterTransition = MaterialElevationScale(false)
//        enterTransition = MaterialElevationScale(false)

//        //val forward: MaterialSharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, true)
//        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//
//        //val backward: MaterialSharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, false)
//        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
//
//        //val forward: MaterialSharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, true)
//        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
//
//        //val backward: MaterialSharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, false)
//        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

//    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//        //return super.onCreateAnimation(transit, enter, nextAnim)
//        return Animation
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding.root.findViewWithTag<ViewGroup>("progressViewRoot")?.let {
            _progressView = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect {
                Timber.d("collecting navigation event: $it")
                findNavController().safeNavigate(it)
                //findNavController().navigate(it)
            }
        }

        _progressView?.let {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.progressViewVisible.collect {
                    Timber.d("changing progress view visibility: $it")
                    setProgressViewEnabled(it)
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.someFlowValue.collect {
//                //do something
//            }
//        }
//
//        OR
//
//        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
//            viewModel.someFlowValue.collect {
//                //do something
//            }
//        }
//
//        OR/specific
//
//        viewModel.someFlowValue.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach {
//                //do something
//            }.launchIn(lifecycleScope)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        if (activity is MainActivity) {
            var  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(_bottomNavigationViewVisibility)
            mainActivity.requestedOrientation = _orientation
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
            mainActivity.setBottomNavigationVisibility(_bottomNavigationViewVisibility)
            mainActivity.requestedOrientation = _orientation
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun BaseFragment<VB, VM>.setProgressViewEnabled(enabled: Boolean) {
        this@BaseFragment._progressView?.apply { visibility = if (enabled) View.VISIBLE else View.GONE }
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