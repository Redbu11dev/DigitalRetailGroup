package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPhoneViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    fun loginWithPhone(phoneRaw: String, phoneFormatted: String) {
        viewModelScope.launch {
            apiRepository.registerPhone(RegisterPhoneDto(phoneRaw)).collect {
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
                        }

                        postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(phoneRaw, phoneFormatted))

                    }
                }
            }
        }
    }

}