package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository): BaseViewModel() {

    //- obtain locally saved profile info
    //- compare it to values in the text fields
    //- if some of them do not match -> let the user save it (both locally and remote)

    //var isRegistration = false

    val name = MutableStateFlow("")
    val surname = MutableStateFlow("")
    val middleName = MutableStateFlow("")
    val birthDate = MutableStateFlow("")
    val genderRadioId = MutableStateFlow(R.id.radio_male)

    init {
        setViewsFromLocalValues()
    }

    fun setViewsFromLocalValues() {
        viewModelScope.launch {
            dataStoreRepository.getUserInfo.collect { localUserInfo ->
                Timber.d("userinfo: ${localUserInfo}")

                val nameToSet = localUserInfo.name
                val surnameToSet = localUserInfo.name
                val middleNameToSet = localUserInfo.name
                val birthDateToSet = localUserInfo.name
                val genderRadioIdToSet = when (localUserInfo.gender) {
                    AppConstants.GenderValues.FEMALE.value -> {
                        R.id.radio_female
                    }
                    else -> {
                        R.id.radio_male
                    }
                }

                name.value = nameToSet
                //delay(100)
                surname.value = surnameToSet
                //delay(100)
                middleName.value = middleNameToSet
                //delay(100)
                birthDate.value = birthDateToSet
                //delay(100)
                genderRadioId.value = genderRadioIdToSet

//                compareLocalValuesToActual(
//                    localUserInfo,
//                    UserInfo(
//                        nameToSet,
//                        surnameToSet,
//                        middleNameToSet,
//                        birthDateToSet,
//                        localUserInfo.gender
//                    )
//                )

                this@launch.cancel()
            }
        }
    }

    private fun compareLocalValuesToActual(localUserInfo: UserInfo, actualUserInfo: UserInfo) {
        Timber.d("local: ${localUserInfo}")
        Timber.d("generated: ${actualUserInfo}")
        if (localUserInfo == actualUserInfo) {
            Timber.d("UserInfo is equal")
            //do nothing
            _profileDataHasChanges.value = false
        }
        else {
            Timber.d("UserInfo is not equal")
            //let the user save it
            _profileDataHasChanges.value = true

        }
    }

    fun profileDataChanged(actualUserInfo: UserInfo) {
        //val generatedUserInfo = UserInfo(name, surname, middleName, birthDate, gender)

        name.value = actualUserInfo.name
        surname.value = actualUserInfo.surname
        middleName.value = actualUserInfo.middle_name
        birthDate.value = actualUserInfo.date_of_birth
        when (actualUserInfo.gender) {
            AppConstants.GenderValues.FEMALE.value -> {
                genderRadioId.value = R.id.radio_female
            }
            else -> {
                genderRadioId.value = R.id.radio_male
            }
        }

        //compare to local
        viewModelScope.launch {
            dataStoreRepository.getUserInfo.collect { localUserInfo ->
                compareLocalValuesToActual(localUserInfo, actualUserInfo)
                this@launch.cancel()
            }
        }
    }


    private val _profileDataHasChanges = MutableStateFlow<Boolean>(false)
    val profileDataHasChanges: StateFlow<Boolean> = _profileDataHasChanges

    fun saveProfileData(userInfo: UserInfo, isRegistration: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.getUserId().collect { userId ->
                apiRepository.saveProfileInfo(Accounts(userId, userInfo)).collect {
                    when (it) {
                        is Resource.Loading -> {
                            //TODO()
                            Timber.i("it's loading")
                            postProgressViewVisibility(true)
                        }
                        is Resource.Error -> {
                            //TODO()
                            this@launch.cancel()
                            val message = it.error?.message
                            Timber.i("it's error: ${message}")
                            //it.error.
                            postProgressViewVisibility(false)
                            postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                        is Resource.Success -> {
                            //TODO()
                            Timber.i("it's success")
                            //check if empty?!
                            it.data?.let { data ->
                                Timber.i("here is the data: $data")

                                dataStoreRepository.saveUserInfo(userInfo)

                                postProgressViewVisibility(false)
                                if (isRegistration) {
                                    postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
                                }
                                else {
                                    //clear the views
                                    setViewsFromLocalValues()
                                }
                                this@launch.cancel()
                            } ?: kotlin.run {
                                this@launch.cancel()
                            }
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreRepository.clearDataStore()
            postNavigationEvent(ProfileFragmentDirections.actionGlobalLogoutToLauncher())
            cancel()
        }
    }


//    private val _resetViewsEvent = MutableSharedFlow<Boolean>(replay = 0)
//    val resetViewsEvent: SharedFlow<Boolean> = _resetViewsEvent
//
//    fun postResetViewsEvent() {
//        viewModelScope.launch {
//            _resetViewsEvent.emit(true)
//            cancel()
//        }
//    }
//
//    private val _profileDataChanged = MutableStateFlow<Boolean>(false)
//    val profileDataChanged: StateFlow<Boolean> = _profileDataChanged
//
//    fun profileDataChanged(generatedUserInfo: UserInfo) {
//        //val generatedUserInfo = UserInfo(name, surname, middleName, birthDate, gender)
//
//        //compare to local
//        viewModelScope.launch {
//            dataStoreRepository.getUserInfo.collect { localUserInfo ->
//                Timber.d("local: ${localUserInfo}")
//                Timber.d("generated: ${generatedUserInfo}")
//                if (localUserInfo == generatedUserInfo) {
//                    Timber.d("UserInfo is equal")
//                    //do nothing
//                    _profileDataChanged.value = false
//                }
//                else {
//                    Timber.d("UserInfo is not equal")
//                    //let the user save it
//                    _profileDataChanged.value = true
//                }
//                this@launch.cancel()
//            }
//        }
//    }
//
//    fun saveProfileData(userInfo: UserInfo, isRegistration: Boolean) {
//        viewModelScope.launch {
//            dataStoreRepository.getUserId().collect { userId ->
//                apiRepository.saveProfileInfo(Accounts(userId, userInfo)).collect {
//                    when (it) {
//                        is Resource.Loading -> {
//                            //TODO()
//                            Timber.i("it's loading")
//                            postProgressViewVisibility(true)
//                        }
//                        is Resource.Error -> {
//                            //TODO()
//                            this@launch.cancel()
//                            val message = it.error?.message
//                            Timber.i("it's error: ${message}")
//                            //it.error.
//                            postProgressViewVisibility(false)
//                            postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
//                        }
//                        is Resource.Success -> {
//                            //TODO()
//                            Timber.i("it's success")
//                            //check if empty?!
//                            it.data?.let { data ->
//                                Timber.i("here is the data: $data")
//
//                                dataStoreRepository.saveUserInfo(userInfo)
//
//                                postProgressViewVisibility(false)
//                                if (isRegistration) {
//                                    postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
//                                }
//                                else {
//                                    //clear the views
//                                    postResetViewsEvent()
//                                }
//                                this@launch.cancel()
//                            } ?: kotlin.run {
//                                this@launch.cancel()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

}