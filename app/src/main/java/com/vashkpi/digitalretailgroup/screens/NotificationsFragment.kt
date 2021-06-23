package com.vashkpi.digitalretailgroup.screens

import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.NotificationsAdapter
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding
import com.vashkpi.digitalretailgroup.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>(FragmentNotificationsBinding::inflate) {

    override val viewModel: NotificationsViewModel by viewModels()

    lateinit var adapter: NotificationsAdapter

    @ExperimentalPagingApi
    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar
        val navController = findNavController()
        toolbar.setupWithNavController(navController)

//        adapter = NotificationsAdapter { view, data ->
//            //fixme need pages
//            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment(data))
//        }
        adapter = NotificationsAdapter({ view, data ->
            //click item listener
            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment(data))
        },
        { view, data ->
            //delete button click listener
            //deleteNotification(data.notification_id)
            viewModel.postNotificationDeletionEvent(data.notification_id)
        })
        //adapter.setHasStableIds(true)

        binding.notificationsList.adapter = adapter
        //(binding.notificationsList.adapter as NotificationsAdapter).mode = Attributes.Mode.Single

        //viewModel.obtainNotifications()

        setFragmentResultListener(ViewNotificationFragment.REQUEST_KEY) { key, bundle ->
            // read from the bundle
            Timber.d("Received fragment result: $bundle")
            if (bundle[ViewNotificationFragment.REQUEST_KEY] == ViewNotificationFragment.RESULT_DELETE) {
//                lifecycleScope.launchWhenResumed {
//                    deleteNotification(bundle[ViewNotificationFragment.NOTIFICATION_ID].toString())
//                }
                  viewModel.postNotificationDeletionEvent(bundle[ViewNotificationFragment.NOTIFICATION_ID].toString())
            }
        }

    }

    @ExperimentalPagingApi
    override fun observeViewModel() {
        super.observeViewModel()

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.event.collectLatest { event ->
//                when (event) {
//                    is NotificationsViewModel.Event.deleteNotification -> {
//                        deleteNotification(event.notificationId)
//                    }
//                }
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is NotificationsViewModel.Event.DeleteNotification -> {
                        deleteNotification(event.notificationId)
                    }
                }
            }
        }

//            viewModel.event.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach { event ->
//                when (event) {
//                    is NotificationsViewModel.Event.deleteNotification -> {
//                        deleteNotification(event.notificationId)
//                    }
//                }
//            }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collectLatest { loadStates ->
                viewModel.decideEmptyContainerVisibility(adapter.itemCount)
                when (loadStates.refresh) {
                    is LoadState.NotLoading -> {
                        viewModel.onListStoppedLoading()
                    }
                    is LoadState.Loading -> {
                        viewModel.onListLoading()
                    }
                    is LoadState.Error -> {
                        viewModel.onListErrorLoading((loadStates.refresh as LoadState.Error).error)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.obtainNotifications().collectLatest { notifications ->
                Timber.i("calling submit data")
                adapter.submitData(notifications)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.emptyContainerVisible.collect {
                if (it) {
                    binding.emptyContainer.visibility = View.VISIBLE
                }
                else {
                    binding.emptyContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun deleteNotification(notificationId: String) {
        viewModel.deleteLocally(notificationId)
        showMessage(
            R.string.snackbar_msg_message_removed,
            R.string.snackbar_btn_cancel,
            {
                //restore notification when clicked
                viewModel.restoreLocally(notificationId)
                adapter.refresh()
            },
            2000,
            object: Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    if (event != BaseCallback.DISMISS_EVENT_ACTION) {
                        viewModel.deleteRemotely(notificationId)
                    }

                    transientBottomBar?.removeCallback(this)
                }
            }
        )
    }

}