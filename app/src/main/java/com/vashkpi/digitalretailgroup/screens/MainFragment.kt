package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.NotificationsAdapter
import com.vashkpi.digitalretailgroup.adapters.PartnersAdapter
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentMainBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    override var bottomNavigationViewVisibility = View.VISIBLE

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val toolbar = binding.customToolbar.toolbar

        toolbar.inflateMenu(R.menu.toolbar_main_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notifications -> {
                    viewModel.postNavigationEvent(MainFragmentDirections.actionNavigationMainToNotificationsFragment())
                    true
                }
                R.id.profile -> {
                    viewModel.postNavigationEvent(MainFragmentDirections.actionNavigationMainToProfileFragment())
                    true
                }
                else -> false
            }
        }

        binding.customToolbar.logo.visibility = View.VISIBLE
        toolbar.title = ""

        val adapter = PartnersAdapter { view, data ->
            viewModel.postNavigationEvent(MainFragmentDirections.actionNavigationMainToBrandInfoFragment())
        }

        binding.partnersList.adapter = adapter

        adapter.setList(arrayListOf("1", "2", "3", "4", "5"))
        adapter.notifyDataSetChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.i("collecting navigation event ${it}")
                    findNavController().safeNavigate(it)
                }
            }
        }

        binding.item1.root.changeAlphaOnTouch()
        binding.item2.root.changeAlphaOnTouch()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}