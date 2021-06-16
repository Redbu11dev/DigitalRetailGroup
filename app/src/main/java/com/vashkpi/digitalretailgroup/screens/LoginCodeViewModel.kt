package com.vashkpi.digitalretailgroup.screens

import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginCodeViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

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