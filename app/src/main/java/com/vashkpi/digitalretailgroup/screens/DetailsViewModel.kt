package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val apiRepository: ApiRepository, private val dataStoreRepository: DataStoreRepository, private val firebaseAnalytics: FirebaseAnalytics) : BaseViewModel() {

    private val _ruleText = MutableStateFlow("")
    val ruleText: StateFlow<String> get() = _ruleText

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            apiRepository.syncNotifications()
//        }
//    }

    fun getSavePointsRules() {
        viewModelScope.launch {
            apiRepository.getSavePointsRules(dataStoreRepository.userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false, 200)
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        postProgressViewVisibility(false, 200)
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.d("here is the data: $data")

                            _ruleText.value = data.rule_text


                        }

                    }
                }
            }
        }
    }

    fun getSpendPointsRules() {
        viewModelScope.launch {
            apiRepository.getSpendPointsRules(dataStoreRepository.userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false, 200)
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        postProgressViewVisibility(false, 200)
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.d("here is the data: $data")

                            _ruleText.value = data.rule_text


                        }

                    }
                }
            }
        }
    }

    fun getPromotionRules() {
        viewModelScope.launch {
            apiRepository.getPromotionRules(dataStoreRepository.userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        postProgressViewVisibility(true)
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        postProgressViewVisibility(false, 200)
                        postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        postProgressViewVisibility(false, 200)
                        //check if empty?!
                        it.data?.let { data ->
                            Timber.d("here is the data: $data")

                            _ruleText.value = data.rule_text


                        }

                    }
                }
            }
        }
    }

    fun logOpenedRulesEvent() {
        firebaseAnalytics.logEvent(AppConstants.FirebaseAnalyticsEvents.OPENED_RULES.value, null)
    }

}