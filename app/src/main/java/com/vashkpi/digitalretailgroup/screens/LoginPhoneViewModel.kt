package com.vashkpi.digitalretailgroup.screens

import com.vashkpi.digitalretailgroup.data.local.PreferencesRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPhoneViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository): BaseViewModel() {

}