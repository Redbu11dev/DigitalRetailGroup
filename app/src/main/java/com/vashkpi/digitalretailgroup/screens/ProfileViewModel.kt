package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.datastore.convertGenderRadioGroupIdToString
import com.vashkpi.digitalretailgroup.data.models.datastore.convertGenderStringToRadioGroupId
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

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> get() = _surname

    private val _middleName = MutableStateFlow("")
    val middleName: StateFlow<String> get() = _middleName

    private val _birthDate = MutableStateFlow("")
    val birthDate: StateFlow<String> get() = _birthDate

    private val _genderRadioId = MutableStateFlow(-1)
    val genderRadioId: StateFlow<Int> get() = _genderRadioId

    private lateinit var _localUserInfo: UserInfo

    init {
        _localUserInfo = dataStoreRepository.userInfo
        Timber.d("obtaining user info: $_localUserInfo")
        setViewsFromLocalValues()
    }

    fun setViewsFromLocalValues() {
        Timber.d("userinfo: ${_localUserInfo}")

        val nameToSet = _localUserInfo.name
        val surnameToSet = _localUserInfo.surname
        val middleNameToSet = _localUserInfo.middle_name
        val birthDateToSet = _localUserInfo.date_of_birth
        val genderRadioIdToSet = _localUserInfo.gender.convertGenderStringToRadioGroupId()

        _name.value = nameToSet
        _surname.value = surnameToSet
        _middleName.value = middleNameToSet
        _birthDate.value = birthDateToSet
        _genderRadioId.value = genderRadioIdToSet

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

        _name.value = actualUserInfo.name
        _surname.value = actualUserInfo.surname
        _middleName.value = actualUserInfo.middle_name
        _birthDate.value = actualUserInfo.date_of_birth
        _genderRadioId.value = actualUserInfo.gender.convertGenderStringToRadioGroupId()

        //compare to local
        compareLocalValuesToActual(_localUserInfo, actualUserInfo)
    }


    private val _profileDataHasChanges = MutableStateFlow<Boolean>(false)
    val profileDataHasChanges: StateFlow<Boolean> = _profileDataHasChanges

    fun saveProfileData(userInfo: UserInfo, isRegistration: Boolean) {
        viewModelScope.launch {
            apiRepository.saveProfileInfo(Accounts(dataStoreRepository.userId, userInfo)).collect {
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

                            dataStoreRepository.userInfo = userInfo

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

    fun logout() {
        dataStoreRepository.clearDataStore()
        postNavigationEvent(ProfileFragmentDirections.actionGlobalLogoutToLauncher())
    }

    private fun createUserInfoFromFields(): UserInfo {
        val userInfo = UserInfo(
            _name.value,
            _surname.value,
            _middleName.value,
            _birthDate.value,
            _genderRadioId.value.convertGenderRadioGroupIdToString())
        Timber.d("viewmodel generated UserInfo: $userInfo")
        return userInfo
    }

    fun onSaveButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            //save directly and navigate to barcode
            saveProfileData(
                createUserInfoFromFields(),
                isRegistration)
        }
        else {
            //show dialog
            postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(isRegistration))
        }
    }

    fun onSecondaryButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            //show dialog
            postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(true))
        }
        else {
            //log out
            //clear datastore and move to launcher fragment
            logout()
        }
    }

    fun onSaveInfoDialogPositiveButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            //as if "save" button was pressed
            saveProfileData(createUserInfoFromFields(), true)
        }
        else {
            saveProfileData(createUserInfoFromFields(), false)
        }
    }

    fun onSaveInfoDialogNegativeButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            //navigate to barcode directly
            postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
            //navigation
            //Navigation.findNavController(requireView()).navigate(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
        }
        else {
            setViewsFromLocalValues()
        }
    }

}