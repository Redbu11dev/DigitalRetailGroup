package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    fun checkIfHasToken() {
        viewModelScope.launch {
            dataStoreRepository.getUserId().collect { userId ->
                this@launch.cancel()
                Timber.i("saved user_id: ${userId}")

                if (userId.isNotBlank()) {
                    //go directly to home screen
                    postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToNavigationBarcode())
                }
                else {
                    postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToLoginPhoneFragment())
                    //postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToNavigationBarcode())
                }
            }
        }
    }

}