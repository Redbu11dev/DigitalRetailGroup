package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository): BaseViewModel() {

    //- obtain locally saved profile info
    //- compare it to values in the text fields
    //- if some of them do not match -> let the user save it (both locally and remote)

    private val _profileDataChanged = MutableStateFlow<Boolean>(false)
    val profileDataChanged: StateFlow<Boolean> = _profileDataChanged

    fun profileDataChanged(generatedUserInfo: UserInfo) {
        //val generatedUserInfo = UserInfo(name, surname, middleName, birthDate, gender)

        //compare to local
        viewModelScope.launch {
            dataStoreRepository.getUserInfo.collect { localUserInfo ->
                Timber.d("local: ${localUserInfo}")
                Timber.d("generated: ${generatedUserInfo}")
                if (localUserInfo == generatedUserInfo) {
                    Timber.d("UserInfo is equal")
                    //do nothing
                }
                else {
                    Timber.d("UserInfo is not equal")
                    //let the user save it
                    _profileDataChanged.value = true
                }
                this@launch.cancel()
            }
        }
    }

    fun saveProfileData(userInfo: UserInfo) {
        viewModelScope.launch {
            dataStoreRepository.saveUserInfo(userInfo)
        }
    }

}