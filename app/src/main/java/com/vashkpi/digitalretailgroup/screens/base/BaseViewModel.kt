package com.vashkpi.digitalretailgroup.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Base view model
 */
abstract class BaseViewModel: ViewModel() {

    private val _errorText = MutableStateFlow("")
    val errorText: StateFlow<String> get() = _errorText

    fun postErrorText(errorText: String) {
        _errorText.value = errorText
    }

    //show or hide progress view
    private val _progressViewVisible = MutableStateFlow(false)
    val progressViewVisible: StateFlow<Boolean> get() = _progressViewVisible

    fun postProgressViewVisibility(visible: Boolean) {
        _progressViewVisible.value = visible
    }

    private val _navigationEvent = MutableSharedFlow<NavDirections>(replay = 0)
    val navigationEvent: SharedFlow<NavDirections> get() = _navigationEvent

    fun postNavigationEvent(action: NavDirections, popBackStack: Boolean = false) {
        viewModelScope.launch {
            _navigationEvent.emit(action)
        }
    }
}