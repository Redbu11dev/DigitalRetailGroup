package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import com.vashkpi.digitalretailgroup.utils.stringSuspending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    //- obtain locally saved profile info
    //- compare it to values in the text fields
    //- if some of them do not match -> let the user save it (both locally and remote)

    fun profileDataChanged(surname: String, name: String, middleName: String, birthDate: String, gender: AppConstants.GenderValues) {

    }

}