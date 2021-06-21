package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(private val apiRepository: ApiRepository, private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> get() = _balance

    private val _code = MutableStateFlow(0)
    val code: StateFlow<Int> get() = _code

    private val newCodeWaitTime: Long = 10000L//TimeUnit.MINUTES.toMillis(1)
    private var newCodeObtainedAtDateMillis: Long = 0

    val countDown: Flow<Long> get() = flow {
        delay(1000)
        while (true) {
            val difference = System.currentTimeMillis() - newCodeObtainedAtDateMillis

            val result = when {
                (difference >= newCodeWaitTime) -> {
                    //time is up
                    if (_viewState.value == ViewState.NEW_CODE_MUST_WAIT) {
                        _viewState.value = ViewState.NEW_CODE_AVAILABLE
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

    fun refreshViewState() {
        when (_balance.value) {
            0 -> {
                _viewState.value = ViewState.ZERO_BALANCE
                //_viewState.value = ViewState.NEW_CODE_AVAILABLE
            }
            else -> {
                if (newCodeObtainedAtDateMillis < (System.currentTimeMillis() - newCodeWaitTime )) {
                    _viewState.value = ViewState.NEW_CODE_AVAILABLE
                }
                else {
                    _viewState.value = ViewState.NEW_CODE_MUST_WAIT
                }
            }
        }
    }

    private val _viewState = MutableStateFlow(ViewState.ZERO_BALANCE)
    val viewState: StateFlow<ViewState> get() = _viewState

    enum class ViewState {ZERO_BALANCE, NEW_CODE_AVAILABLE, NEW_CODE_MUST_WAIT}

    fun getPromotionRules() {
        viewModelScope.launch {
            apiRepository.getPromotionRules(dataStoreRepository.userId).collect {
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
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.i("here is the data: $data")

                            postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToDetailsFragment(R.string.barcode_offer_rules, data.rule_text))

                            postProgressViewVisibility(false)

                            this@launch.cancel()
                        } ?: kotlin.run {
                            this@launch.cancel()
                        }
                    }
                }
            }
        }
    }

    fun getNewCode() {
        viewModelScope.launch {
            apiRepository.getCode(dataStoreRepository.userId).collect {
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
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.i("here is the data: $data")

                            val code = data.code
                            _code.value = code

                            newCodeObtainedAtDateMillis = System.currentTimeMillis()
                            refreshViewState()

                            postProgressViewVisibility(false)

                            this@launch.cancel()
                        } ?: kotlin.run {
                            this@launch.cancel()
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
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        this@launch.cancel()
                        val message = it.error?.message
                        Timber.i("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false)
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.i("it's success")
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.i("here is the data: $data")

                            //val balance = data.balance
                            val balance = 1 //fixme temp
                            _balance.value = balance
                            refreshViewState()

                            postProgressViewVisibility(false)

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