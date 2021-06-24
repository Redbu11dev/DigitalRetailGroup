package com.vashkpi.digitalretailgroup.screens

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.*
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.adapters.NotificationsAdapter
import com.vashkpi.digitalretailgroup.data.api.ApiService
import com.vashkpi.digitalretailgroup.data.api.NotificationsPagingSource
import com.vashkpi.digitalretailgroup.data.api.NotificationsRemoteMediator
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.network.asDomainModel
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentNotificationsBinding
import com.vashkpi.digitalretailgroup.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>(FragmentNotificationsBinding::inflate) {

    override val viewModel: NotificationsViewModel by viewModels()

    private val adapter: NotificationsAdapter = NotificationsAdapter({ view, data ->
        //click item listener
        findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment(data))
    },
        { view, data ->
            //delete button click listener
            //deleteNotification(data.notification_id)
            lifecycleScope.launchWhenResumed {
                viewModel.postNotificationDeletionEvent(data.notification_id)
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("I am recreated")

        lifecycleScope.launchWhenCreated {
            viewModel.obtainNotifications().collectLatest { notifications ->
                Timber.d("calling submit data")
                adapter.submitData(notifications)
                viewModel.decideEmptyContainerVisibility(adapter.itemCount)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.syncNotifications()
    }

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
//        adapter = NotificationsAdapter({ view, data ->
//            //click item listener
//            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToViewNotificationFragment(data))
//        },
//        { view, data ->
//            //delete button click listener
//            //deleteNotification(data.notification_id)
//            lifecycleScope.launchWhenResumed {
//                viewModel.postNotificationDeletionEvent(data.notification_id)
//            }
//        })

        binding.notificationsList.adapter = adapter
        //(binding.notificationsList.adapter as NotificationsAdapter).mode = Attributes.Mode.Single

        //viewModel.obtainNotifications()

//        runBlocking {
//            adapter.submitData(lifecycle,
//                Pager(
//                    // Configure how data is loaded by passing additional properties to
//                    // PagingConfig, such as prefetchDistance.
//                    PagingConfig(pageSize = 10, initialLoadSize = 10)
//                    //remoteMediator = NotificationsRemoteMediator(dataStoreRepository.userId, appDatabase, apiService)
//                ) {
//                    NotificationsPagingSource(apiService, dataStoreRepository.userId)
//                    //appDatabase.notificationDao().pagingSourceOfNotRemoved()
//
//                }.flow.map { it.map { it.asDomainModel() } }.first())
//        }

        setFragmentResultListener(ViewNotificationFragment.REQUEST_KEY) { key, bundle ->
            // read from the bundle
            Timber.d("Received fragment result: $bundle")
            if (bundle[ViewNotificationFragment.REQUEST_KEY] == ViewNotificationFragment.RESULT_DELETE) {
                lifecycleScope.launchWhenResumed {
                    viewModel.postNotificationDeletionEvent(bundle[ViewNotificationFragment.NOTIFICATION_ID].toString())
                }
                //deleteNotification(bundle[ViewNotificationFragment.NOTIFICATION_ID].toString())
            }
        }

//        Timber.d("I am recreated")

    }

    @ExperimentalPagingApi
    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collectLatest { event ->
                when (event) {
                    is NotificationsViewModel.Event.DeleteNotification -> {
                        deleteNotification(event.notificationId)
                    }
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.events.collect { event ->
//                when (event) {
//                    is NotificationsViewModel.Event.DeleteNotification -> {
//                        deleteNotification(event.notificationId)
//                    }
//                }
//            }
//        }

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
//                viewModel.decideEmptyContainerVisibility(adapter.itemCount)
                when (loadStates.refresh) {
                    is LoadState.NotLoading -> {
                        viewModel.onListStoppedLoading()
                        //viewModel.decideEmptyContainerVisibility(adapter.itemCount)
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

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.obtainNotifications().collectLatest { notifications ->
//                Timber.i("calling submit data")
//                adapter.submitData(notifications)
//            }
//        }

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
            3000,
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