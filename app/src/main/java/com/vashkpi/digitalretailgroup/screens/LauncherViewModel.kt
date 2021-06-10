package com.vashkpi.digitalretailgroup.screens

import com.vashkpi.digitalretailgroup.data.local.PreferencesRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository): BaseViewModel() {
    fun checkIfHasToken() {
        Timber.i("saved token: ${preferencesRepository.token}")
        if (preferencesRepository.token.isNotBlank()) {
            //go directly to home screen
            postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToNavigationBarcode())
        }
        else {
            postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToLoginPhoneFragment())
            //postNavigationEvent(LauncherFragmentDirections.actionLauncherFragmentToNavigationBarcode())
        }
    }
}