package com.vashkpi.digitalretailgroup.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    private val _navigationEvent = MutableSharedFlow<NavDirections>(replay = 0)
    val navigationEvent: SharedFlow<NavDirections> = _navigationEvent

    fun postNavigationEvent(action: NavDirections) {
        viewModelScope.launch {
            _navigationEvent.emit(action)
            cancel()
        }
    }
}