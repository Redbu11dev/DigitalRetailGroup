package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import com.vashkpi.digitalretailgroup.utils.stringSuspending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPhoneViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    fun loginWithPhone(phone: String) {
        viewModelScope.launch {
            apiRepository.registerPhone(RegisterPhone(phone)).collect {
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
                        //check if empty?!
                        it.data?.let {
                            Timber.d("here is the data: $it")
                        }
                        postProgressViewVisibility(false)
                        postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(phone))
                    }
                }
            }
            //cancel()
        }
    }

}