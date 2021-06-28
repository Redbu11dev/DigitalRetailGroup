package com.vashkpi.digitalretailgroup.screens

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginCodeViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository,
                                             private val apiRepository: ApiRepository,
private val firebaseAnalytics: FirebaseAnalytics): BaseViewModel() {

    fun confirmCode(phone: String, code: String) {
        viewModelScope.launch {
            apiRepository.confirmCode(ConfirmCodeDto(phone, code, dataStoreRepository.deviceId, AppConstants.DEVICE_OS)).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        val message = it.error?.message ?: ""
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false)

                        if (message.startsWith("404: ")) {
                            postErrorText(message.replace("404: ", ""))
                        }
                        else {
                            postNavigationEvent(LoginCodeFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message))
                        }
                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        postProgressViewVisibility(false)
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.d("here is the data: $data")

                            //obtain user id from data
                            val userId = data.user_id
                            //save it
                            dataStoreRepository.userId = userId
                            dataStoreRepository.userPhone = phone


                            firebaseAnalytics.logEvent(AppConstants.FirebaseAnalyticsEvents.USER_AUTHORIZED.value, null)
                            postNavigationEvent(LoginCodeFragmentDirections.actionLoginCodeFragmentToProfileFragment(true, userId))
                        }
                    }
                }
            }
        }
    }

    fun sendSmsAgain(phone: String) {
        viewModelScope.launch {
            apiRepository.registerPhone(RegisterPhoneDto(phone)).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false)
                        postNavigationEvent(LoginPhoneFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))

                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        postProgressViewVisibility(false)
                        //check if empty?!
                        it.data?.let {
                            Timber.d("here is the data: $it")

                            postNavigationEvent(LoginPhoneFragmentDirections.actionGlobalMessageDialog(title = 0, message = it.message))
                        }


                    }
                }
            }
        }
    }

}