package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    fun checkIfHasToken() {
        viewModelScope.launch {
            dataStoreRepository.getAuthToken().firstOrNull()?.let { authToken ->
                Timber.i("saved token: ${authToken}")

                if (authToken.isNotBlank()) {
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