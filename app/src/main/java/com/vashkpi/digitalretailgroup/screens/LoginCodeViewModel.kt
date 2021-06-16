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
                            val message = it.error?.message
                            Timber.i("it's error: ${message}")
                            //it.error.
                            postProgressViewVisibility(false)
                            //postErrorMessageOneShot(message.toString())
                            postNavigationEvent(LoginCodeFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                        is Resource.Success -> {
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
            }
            //cancel()
        }
    }

//    fun confirmCode(phone: String, code: String) {
//        //_progressViewVisible.value = true
//        viewModelScope.launch {
//
//            val loginApiResponse =
//                apiRepository.confirmCode(ConfirmCode(phone, code, preferencesRepository.fcmToken, AppConstants.DEVICE_PLATFORM))
//
//            //_progressViewVisible.value = false
//
//            loginApiResponse.response?.let { response ->
//                if (response.isSuccessful) {
//                    //val loginResponseData = Gson().fromJson(response.body()?.asJsonObject?.getAsJsonPrimitive("data").asString, String::class.java)
//                    //response.body()?.asJsonObject?.getAsJsonPrimitive("data")!!.asString
//
//                    //postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(phone))
//                    //navigate to profile screen
//                    postNavigationEvent(LoginCodeFragmentDirections.actionLoginCodeFragmentToProfileFragment(true))
//                }
//                else {
//                    //val errors = Gson().fromJson(response.errorBody()?.stringSuspending(), ApiError::class.java)
////                    val errorMessage = try {
////                        errors.error.last().message
////                    } catch (e: Exception) {
////                        e.message
////                    }
//                    //_errorText.value = errorMessage.toString()
//                }
//            }?:run {
//                //_errorText.value = loginApiResponse.error.toString()
//            }
//        }
//    }

}