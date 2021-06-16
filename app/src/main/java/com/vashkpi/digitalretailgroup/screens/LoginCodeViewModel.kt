package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginCodeViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository): BaseViewModel() {

    fun confirmCode(phone: String, code: String) {
        viewModelScope.launch {
            dataStoreRepository.getFcmToken().collect { fcmToken ->
                apiRepository.confirmCode(ConfirmCode(phone, code, fcmToken, AppConstants.DEVICE_PLATFORM)).collect {
                    when (it) {
                        is Resource.Loading -> {
                            Timber.i("it's loading")
                            postProgressViewVisibility(true)
                        }
                        is Resource.Error -> {
                            this@launch.cancel()
                            val message = it.error?.message
                            Timber.i("it's error: ${message}")
                            //it.error.
                            postProgressViewVisibility(false)
                            //postErrorMessageOneShot(message.toString())
                            postNavigationEvent(LoginCodeFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                        is Resource.Success -> {
                            this@launch.cancel()
                            Timber.i("it's success")
                            //check if empty?!
                            it.data?.let {
                                Timber.i("here is the data: $it")
                            }
                            postProgressViewVisibility(false)
                            postNavigationEvent(LoginCodeFragmentDirections.actionLoginCodeFragmentToProfileFragment(true))
                        }
                    }
                }
                this@launch.cancel()
            }
            //cancel()
        }
    }

}