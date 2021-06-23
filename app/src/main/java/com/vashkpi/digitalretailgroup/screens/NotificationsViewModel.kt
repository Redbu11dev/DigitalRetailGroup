package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.data.models.network.asDomainModel
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository): BaseViewModel() {

//    private val _notificationsList = MutableStateFlow(mutableListOf<Notification>())
//    val notificationsList: StateFlow<List<Notification>> get() = _notificationsList

    private val _notificationsList = MutableStateFlow<PagingData<Notification>?>(null)
    val notificationsList: StateFlow<PagingData<Notification>?> get() = _notificationsList

    private val _emptyContainerVisible = MutableStateFlow(false)
    val emptyContainerVisible: StateFlow<Boolean> get() = _emptyContainerVisible

    @ExperimentalPagingApi
    fun obtainNotifications(): Flow<PagingData<Notification>> {

//        viewModelScope.launch {
//            apiRepository.getNotifications()
//                .map { pagingData ->
//                    pagingData.map {
//                        it.asDomainModel()
//                    }
//                }
//        }


        return apiRepository.getNotifications()
            .map { pagingData ->
                pagingData.map {
                    it.asDomainModel()
                }
            }
            //.cachedIn(viewModelScope)
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

}