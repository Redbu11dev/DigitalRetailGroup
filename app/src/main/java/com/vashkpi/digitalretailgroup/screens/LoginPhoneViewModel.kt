package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.local.PreferencesRepository
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import com.vashkpi.digitalretailgroup.utils.stringSuspending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPhoneViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository, private val apiRepository: ApiRepository): BaseViewModel() {

    fun lp(phone: String) {
        viewModelScope.launch {
            apiRepository.registerPhoneNetBoRes(RegisterPhone(phone)).collect {

            }
        }
    }

    //val something = apiRepository.registerPhoneNetBoRes(RegisterPhone(phone))

    /**
     * Login
     */
    fun loginWithPhone(phone: String) {
        //_progressViewVisible.value = true
        viewModelScope.launch {
            val loginApiResponse =
                apiRepository.registerPhone(RegisterPhone(phone))

            //_progressViewVisible.value = false

            loginApiResponse.response?.let { response ->
                if (response.isSuccessful) {
                    //val loginResponseData = Gson().fromJson(response.body()?.asJsonObject?.getAsJsonPrimitive("data").asString, String::class.java)
                    //response.body()?.asJsonObject?.getAsJsonPrimitive("data")!!.asString

                    postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(phone))
                }
                else {
                    //val errors = Gson().fromJson(response.errorBody()?.stringSuspending(), ApiError::class.java)
//                    val errorMessage = try {
//                        errors.error.last().message
//                    } catch (e: Exception) {
//                        e.message
//                    }
                    //_errorText.value = errorMessage.toString()
                }
            }?:run {
                //_errorText.value = loginApiResponse.error.toString()
            }
        }
    }

}