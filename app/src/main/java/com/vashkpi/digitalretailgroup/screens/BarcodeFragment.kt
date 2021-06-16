package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentBarcodeBinding
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BarcodeFragment : BaseFragment<FragmentBarcodeBinding, BarcodeViewModel>() {

    override var bottomNavigationViewVisibility = View.VISIBLE

    //private val viewModel: BarcodeViewModel by viewModels()
    override val viewModel: BarcodeViewModel by viewModels()

    private var _binding: FragmentBarcodeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val toolbar = binding.customToolbar.toolbar

        toolbar.inflateMenu(R.menu.toolbar_main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notifications -> {
                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
                    true
                }
                R.id.profile -> {
                    viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToProfileFragment(false))
                    true
                }
                else -> false
            }
        }

        binding.customToolbar.logo.visibility = View.VISIBLE
        toolbar.title = ""

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.i("collecting navigation event ${it}")
                    findNavController().safeNavigate(it)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}