package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
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
class BarcodeViewModel @Inject constructor(private val apiRepository: ApiRepository, private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> get() = _balance

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

    fun setViewsAccordingToBalance(balance: Int) {
        when (balance) {
            0 -> {
                _viewState.value = ViewState.ZERO_BALANCE
            }
            else -> {
                _viewState.value = ViewState.NEW_CODE_AVAILABLE
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

                            val balance = data.balance
                            _balance.value = balance
                            setViewsAccordingToBalance(balance)

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