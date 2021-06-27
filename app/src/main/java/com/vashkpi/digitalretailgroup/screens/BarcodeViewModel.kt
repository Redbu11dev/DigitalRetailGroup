package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.domain.DeviceInfo
import com.vashkpi.digitalretailgroup.data.models.domain.asNetworkModel
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import com.vashkpi.digitalretailgroup.utils.obtainFcmToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(private val apiRepository: ApiRepository, private val dataStoreRepository: DataStoreRepository, private val firebaseAnalytics: FirebaseAnalytics): BaseViewModel() {

    private val _barcodeValue = MutableStateFlow(dataStoreRepository.userPhone)
    val barcodeValue: StateFlow<String> get() = _barcodeValue

    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> get() = _balance

    private val _code = MutableStateFlow(dataStoreRepository.lastObtainedCode)
    val code: StateFlow<Int> get() = _code

    private val newCodeWaitTime: Long =
                                        //AppConstants.NEW_CODE_TIMEOUT_MILLIS
                                        10000L
    private var newCodeObtainedAtDateMillis: Long = dataStoreRepository.newCodeObtainedAtDateMillis

    val countDown: Flow<Long> get() = flow {
        delay(500)
        while (true) {
            val difference = System.currentTimeMillis() - newCodeObtainedAtDateMillis

            val result = when {
                (difference >= newCodeWaitTime) -> {
                    //time is up
                    if (_codeViewState.value == CodeViewState.NEW_CODE_MUST_WAIT) {
                        _codeViewState.value = CodeViewState.NEW_CODE_AVAILABLE
                    }
                    0L
                }
                else -> {
                    //less than a minute left
                    newCodeWaitTime - difference
                }
            }

            emit(result)
            delay(1000)
        }
    }

    init {
        trySaveDeviceInfoOnServer()
    }

    fun refreshViewState() {
        when {
            balance.value == 0 -> {
                _codeViewState.value = CodeViewState.UNAVAILABLE
            }
            _code.value == 0 -> {
                _codeViewState.value = CodeViewState.NEW_CODE_AVAILABLE_NEVER_OBTAINED
            }
            newCodeObtainedAtDateMillis < (System.currentTimeMillis() - newCodeWaitTime ) -> {
                _codeViewState.value = CodeViewState.NEW_CODE_AVAILABLE
            }
            else -> {
                _codeViewState.value = CodeViewState.NEW_CODE_MUST_WAIT
            }
        }
    }

    private val _balanceViewState = MutableStateFlow(BalanceViewState.LOADING)
    val balanceViewState: StateFlow<BalanceViewState> get() = _balanceViewState

    enum class BalanceViewState {LOADING, ERROR, OBTAINED}

    private val _codeViewState = MutableStateFlow(CodeViewState.UNAVAILABLE)
    val codeViewState: StateFlow<CodeViewState> get() = _codeViewState

    enum class CodeViewState {UNAVAILABLE, NEW_CODE_AVAILABLE_NEVER_OBTAINED, NEW_CODE_AVAILABLE, NEW_CODE_MUST_WAIT}

    fun getNewCode() {
        viewModelScope.launch {
            apiRepository.getCode(dataStoreRepository.userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.i("it's loading")
                        postProgressViewVisibility(true)
                        refreshViewState()
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.i("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false)
                        postNavigationEvent(BarcodeFragmentDirections.actionGlobalMessageDialog(R.string.dialog_error_title, message.toString()))
                        refreshViewState()
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.i("here is the data: $data")

                            val code = data.code
                            _code.value = code

                            newCodeObtainedAtDateMillis = System.currentTimeMillis()
                            dataStoreRepository.newCodeObtainedAtDateMillis = newCodeObtainedAtDateMillis
                            dataStoreRepository.lastObtainedCode = data.code
                            refreshViewState()

                            postProgressViewVisibility(false)
                        }
                    }
                }
            }
        }
    }

    fun getBalance() {
        viewModelScope.launch {
            apiRepository.getBalance(dataStoreRepository.userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.i("it's loading")
                        //postProgressViewVisibility(true)
                        refreshViewState()
                        _balanceViewState.value = BalanceViewState.LOADING
                    }
                    is Resource.Error -> {
                        val message = it.error.message
                        Timber.i("it's error: ${message}")
                        //postProgressViewVisibility(false)
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        refreshViewState()

                        delay(500)
                        _balanceViewState.value = BalanceViewState.ERROR

                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        Timber.i("here is the data: ${it.data}")

                        it.data?.let {
                            //val balance = it.balance
                            val balance = 1 //mock
                            _balance.value = balance

                            refreshViewState()

                            delay(500)
                            _balanceViewState.value = BalanceViewState.OBTAINED

                            //postProgressViewVisibility(false)
                        }

                    }
                }
            }
        }
    }

    fun onHowToGetPointsBtnClick() {
        postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToDetailsFragment(0))
    }

    fun onPromotionRulesClick() {
        postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToDetailsFragment(2))
    }

    private fun saveDeviceInfoOnServer(newFcmToken: String?) {

        val fcmTokenToUse = newFcmToken ?: dataStoreRepository.fcmToken

        val savedDeviceInfo = dataStoreRepository.savedDeviceInfo
        val currentDeviceInfo = DeviceInfo(
            dataStoreRepository.userId,
            fcmTokenToUse,
            dataStoreRepository.deviceId,
            AppConstants.DEVICE_OS
        )

        if (savedDeviceInfo != currentDeviceInfo) {
            //attempt to save new device info on the server
            viewModelScope.launch {
                apiRepository.saveDeviceInfo(currentDeviceInfo.asNetworkModel()).collect {
                    when (it) {
                        is Resource.Loading -> {
                            Timber.d("attempting to store new device info")
                        }
                        is Resource.Error -> {
                            val message = it.error?.message
                            Timber.d("Unable to store new device info: ${message}")
                        }
                        is Resource.Success -> {
                            Timber.d("it's success")

                            dataStoreRepository.savedDeviceInfo = currentDeviceInfo

                            //check if empty
                            it.data?.let { data ->
                                Timber.d("here is the data: $data")
                            }
                        }
                    }
                }
            }

        }
    }

    fun trySaveDeviceInfoOnServer() {
        obtainFcmToken(
            {fcmToken ->
                dataStoreRepository.fcmToken = fcmToken
                saveDeviceInfoOnServer(fcmToken)
            },
            {
                //unable to obtain token
                //do nothing?
                saveDeviceInfoOnServer(null)
            }
        )

    }

    fun logOpenedPurseEvent() {
        firebaseAnalytics.logEvent(AppConstants.FirebaseAnalyticsEvents.OPENED_PURSE.value, null)
    }

}