package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.incoming.Brand
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
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
class MainViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    private val _brandsList = MutableStateFlow(arrayListOf<Brand>())
    val brandsList: StateFlow<ArrayList<Brand>> get() = _brandsList

    fun onMenuNotificationsItemClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToNotificationsFragment())
    }

    fun onMenuProfileItemClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToProfileFragment(false, ""))
    }

    fun onBrandsListItemClick(brand: Brand) {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToBrandInfoFragment(brand))
    }

    fun obtainBrands() {
        viewModelScope.launch {
            apiRepository.getBrands().collect {
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

                            _brandsList.value = data.elements

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