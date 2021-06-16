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



}