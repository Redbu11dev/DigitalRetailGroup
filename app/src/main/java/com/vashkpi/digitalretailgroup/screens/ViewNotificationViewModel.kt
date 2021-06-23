package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.data.models.network.NotificationPostDto
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewNotificationViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository): BaseViewModel() {

//    private var _notification = MutableStateFlow<Notification?>(null)
//    val notification: StateFlow<Notification?> get() = _notification
//
//    fun setNotification(notification: Notification) {
//        _notification.value = notification
//    }
//
//    //var _page: Int? = null
//
//    fun tryMarkRead() {
//        _notification.value?.let {
//            markRead(it)
//        }
//    }

    private val _notificationRemovedEvent = MutableSharedFlow<Boolean>(replay = 0)
    val notificationRemovedEvent: SharedFlow<Boolean> get() = _notificationRemovedEvent

    fun markRead(notification: Notification) {
        viewModelScope.launch {
            apiRepository.markNotificationRead(NotificationPostDto(dataStoreRepository.userId, notification.notification_id)).collect {
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
                            //no internet connection - show nothing
                        }
                        else {
                            postNavigationEvent(ViewNotificationFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!
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



    fun onDeletePressed() {
        viewModelScope.launch {
            _notificationRemovedEvent.emit(true)
            cancel()
        }
    }

}