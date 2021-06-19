package com.vashkpi.digitalretailgroup.screens

import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    fun checkIfHasToken() {
        val userId = dataStoreRepository.userId

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