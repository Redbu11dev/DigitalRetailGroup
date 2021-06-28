package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.network.asDatabaseModel
import com.vashkpi.digitalretailgroup.data.models.network.asDomainModel
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
class MainViewModel @Inject constructor(private val apiRepository: ApiRepository, private val dataStoreRepository: DataStoreRepository): BaseViewModel() {

    private val _brandsList = MutableStateFlow(mutableListOf<Brand>())
    val brandsList: StateFlow<List<Brand>> get() = _brandsList

    private val _brandsListLoading = MutableStateFlow(true)
    val brandsListLoading: StateFlow<Boolean> get() = _brandsListLoading

    fun onMenuNotificationsItemClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToNotificationsFragment())
    }

    fun onMenuProfileItemClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToProfileFragment(false, ""))
    }

    fun onBrandsListItemClick(brand: Brand) {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToBrandInfoFragment(brand))
    }

    init {
        obtainBrands()
    }

    fun obtainBrands() {
        viewModelScope.launch {
            apiRepository.getBrands().collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
                        //postProgressViewVisibility(true)
                        _brandsListLoading.value = true
//                        it.data?.let { data ->
//                            Timber.d("here is the old data: $data")
//
//                            _brandsList.value = data.map { it.asDomainModel() }.toMutableList()
//                        }
                    }
                    is Resource.Error -> {
                        val message = it.error?.message
                        Timber.d("it's error: ${message}")
                        //it.error.
                        //postProgressViewVisibility(false)
                        _brandsListLoading.value = false
                        if (it.error !is java.net.UnknownHostException) {
                            postNavigationEvent(ProfileFragmentDirections.actionGlobalMessageDialog(title = R.string.dialog_error_title, message = message.toString()))
                        }
                    }
                    is Resource.Success -> {
                        Timber.d("it's success")
                        //check if empty?!
                        //postProgressViewVisibility(false)
                        _brandsListLoading.value = false
                        it.data?.let { data ->
                            Timber.d("here is the data: $data")

//                            _brandsList.value = data.map { it.asDomainModel() }.toMutableList()
                            _brandsList.value = data.elements.map { it.asDomainModel() }.toMutableList()
                        }
                    }
                }
            }
        }
    }

    fun onSavePointsOptionClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToDetailsFragment(0))
    }

    fun onSpendPointsOptionClick() {
        postNavigationEvent(MainFragmentDirections.actionNavigationMainToDetailsFragment(1))
    }

}