package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val apiRepository: ApiRepository,
    private val appDatabase: AppDatabase
    ): BaseViewModel() {

//    private val _notificationsList = MutableStateFlow(mutableListOf<Notification>())
//    val notificationsList: StateFlow<List<Notification>> get() = _notificationsList

    private val _notificationsList = MutableStateFlow<PagingData<Notification>?>(null)
    val notificationsList get() = _notificationsList.asStateFlow()

    private val _emptyContainerVisible = MutableStateFlow(false)
    val emptyContainerVisible get() = _emptyContainerVisible.asStateFlow()

    sealed class Event {
        data class DeleteNotification(val notificationId: String) : Event()
    }

    private val _event = MutableSharedFlow<Event>(replay = 0)
    val event get() = _event.asSharedFlow()

    suspend fun postNotificationDeletionEvent(notificationId: String) {
        _event.emit(Event.DeleteNotification(notificationId))
    }

//    init {
//        viewModelScope.launch {
//            apiRepository.syncNotifications()
//        }
//    }

    fun syncNotifications() {
        viewModelScope.launch {
            apiRepository.syncNotifications()
        }
    }

    @ExperimentalPagingApi
    fun obtainNotifications(): Flow<PagingData<Notification>> {
        return apiRepository.getNotifications()
            .map { pagingData ->
                pagingData.map {
                    it.asDomainModel()
                }
            }
            .cachedIn(viewModelScope)
    }

    fun decideEmptyContainerVisibility(itemCount: Int) {
        _emptyContainerVisible.value = itemCount < 1
    }

    fun onListStoppedLoading() {
        postProgressViewVisibility(false)
    }

    fun onListErrorLoading(throwable: Throwable) {
        postProgressViewVisibility(false)
        //show error
        if (throwable is java.net.UnknownHostException) {
            //no internet connection - show nothing
        }
        else {
            postNavigationEvent(NotificationsFragmentDirections.actionGlobalMessageDialog(
                title = R.string.dialog_error_title,
                message = throwable.message.toString()
            ))
        }
    }

    fun onListLoading() {
        postProgressViewVisibility(true)
    }

//    fun obtainNotifications() {
//        viewModelScope.launch {
//            apiRepository.getNotifications(dataStoreRepository.userId, 1).collect {
//                when (it) {
//                    is Resource.Loading -> {
//                        Timber.i("it's loading")
//                        postProgressViewVisibility(true)
//                    }
//                    is Resource.Error -> {
//                        this@launch.cancel()
//                        val message = it.error?.message
//                        Timber.i("it's error: ${message}")
//                        //it.error.
//                        postProgressViewVisibility(false)
//                        postNavigationEvent(NotificationsFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
//                    }
//                    is Resource.Success -> {
//                        Timber.i("it's success")
//                        //check if empty?!
//                        it.data?.let { data ->
//                            Timber.i("here is the data: $data")
//
//                            _emptyContainerVisible.value = data.isEmpty()
//
//                            _notificationsList.value = data.map { it.asDomainModel() }.toMutableList()
//
//                            postProgressViewVisibility(false)
//
//                            this@launch.cancel()
//                        } ?: kotlin.run {
//                            this@launch.cancel()
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun deleteLocally(notificationId: String) {
        viewModelScope.launch {
            apiRepository.deleteNotificationLocally(notificationId)
            cancel()
        }
    }

    fun restoreLocally(notificationId: String) {
        viewModelScope.launch {
            apiRepository.restoreNotificationLocally(notificationId)
            cancel()
        }
    }

    fun deleteRemotely(notificationId: String) {
        viewModelScope.launch {
            apiRepository.deleteNotificationRemotely(dataStoreRepository.userId, notificationId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.i("it's loading")
                        //postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        this@launch.cancel()
                        val message = it.error?.message
                        Timber.i("it's error: ${message}")
                        //it.error.
                        //postProgressViewVisibility(false)

                        if (it.error is java.net.UnknownHostException) {
                            //no internet connection - show nothing, proceed as if success
                            onDeletionSuccess()
                        }
                        else {
                            postNavigationEvent(ViewNotificationFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!

                        onDeletionSuccess()

                        it.data?.let { data ->
                            Timber.i("here is the data: $data")

                            //postProgressViewVisibility(false)

                            this@launch.cancel()
                        } ?: kotlin.run {
                            this@launch.cancel()
                        }
                    }
                }
            }
        }
    }

    fun onDeletionSuccess() {
        //remove from database
    }

}