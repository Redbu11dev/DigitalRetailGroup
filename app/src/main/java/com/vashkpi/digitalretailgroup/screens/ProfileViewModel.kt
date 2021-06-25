package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.domain.*
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.data.models.network.asDomainModel
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val apiRepository: ApiRepository, private val appDatabase: AppDatabase, private val firebaseAnalytics: FirebaseAnalytics): BaseViewModel() {

    //- obtain locally saved profile info
    //- compare it to values in the text fields
    //- if some of them do not match -> let the user save it (both locally and remote)

    private val _localUserInfo = MutableStateFlow<UserInfo?>(null)
    val localUserInfo: StateFlow<UserInfo?> get() = _localUserInfo

    init {
        getProfileInfoFromServer(dataStoreRepository.userId)
    }

    fun getProfileInfoFromServer(userId: String) {
        viewModelScope.launch {
            apiRepository.getProfileInfo(userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                        //this@launch.cancel()

                        _localUserInfo.value = dataStoreRepository.userInfo
                    }
                    is Resource.Error -> {
                        this@launch.cancel()
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false)

                        //TODO-check: if error message is "message": "Отсутствует параметр user_id" then user does not exist in the server db

                        when (it.error) {
                            is java.net.UnknownHostException -> {
                                //no internet - serve cache
                                _localUserInfo.value = dataStoreRepository.userInfo
                            }
                            else -> {
                                //other errors - show message dialog
                                postNavigationEvent(LoginPhoneFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                            }
                        }

                    }
                    is Resource.Success -> {
                        this@launch.cancel()
                        Timber.d("it's success")
                        //check if empty?!
                        it.data?.let {
                            Timber.d("here is the data: $it")

                            val obtainedUserInfo = it.user_info.asDomainModel()

                            //set from this info
                            _localUserInfo.value = obtainedUserInfo

                            //cache after get
                            dataStoreRepository.userInfo = obtainedUserInfo
                        }
                        postProgressViewVisibility(false)

                    }
                }
            }
            //cancel()
        }
    }

    fun notifyProfileDataChanged(userInfo: UserInfo) {
        _localUserInfo.value = userInfo
        compareLocalValuesToActual()
    }

    private fun compareLocalValuesToActual() {
        val cached = dataStoreRepository.userInfo
        val current = _localUserInfo.value

        Timber.d("cached: ${cached}")
        Timber.d("current: ${current}")

        if (cached == current) {
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

    private val _profileDataHasChanges = MutableStateFlow<Boolean>(false)
    val profileDataHasChanges: StateFlow<Boolean> = _profileDataHasChanges

    fun saveProfileData(userInfo: UserInfo, isRegistration: Boolean) {
        viewModelScope.launch {
            apiRepository.saveProfileInfo(AccountsDto(dataStoreRepository.userId, userInfo.asNetworkModel())).collect {
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

                            postProgressViewVisibility(false)
                            firebaseAnalytics.logEvent(AppConstants.FirebaseAnalyticsEvents.PROFILE_FILLED.value, null)
                            if (isRegistration) {
                                postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
                            }
                            else {
                                //update the cache
                                dataStoreRepository.userInfo = userInfo
                                //compare values (to show/hide save button)
                                compareLocalValuesToActual()
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
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.clearDataStore()
            appDatabase.clearAllTables()
            postNavigationEvent(ProfileFragmentDirections.actionGlobalLogoutToLauncher())
        }
    }

    fun onSaveButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            //save directly and navigate to barcode
            _localUserInfo.value?.let {
                saveProfileData(it, isRegistration)
            }

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
            _localUserInfo.value?.let {
                saveProfileData(it, true)
            }
        }
        else {
            _localUserInfo.value?.let {
                saveProfileData(it, false)
            }
        }
    }

    fun onSaveInfoDialogNegativeButtonClick(isRegistration: Boolean) {
        if (isRegistration) {
            postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
        }
        else {
            //drop changes (reset to local cache)
            _localUserInfo.value = dataStoreRepository.userInfo
        }
    }

}