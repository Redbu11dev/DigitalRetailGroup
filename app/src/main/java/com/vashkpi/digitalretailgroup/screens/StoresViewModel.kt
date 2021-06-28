package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Store
import com.vashkpi.digitalretailgroup.data.models.network.asDomainModel
import com.vashkpi.digitalretailgroup.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    private val _storesList = MutableStateFlow(mutableListOf<Store>())
    val storesList: StateFlow<List<Store>> get() = _storesList

    fun obtainRegionStores(brandId: String, regionId: String) {
        viewModelScope.launch {
            apiRepository.getRegionStores(brandId, regionId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
//                        it.data?.let { data ->
//                            Timber.d("here is the old data: $data")
//
//                            _storesList.value = data.map { it.asDomainModel() }.toMutableList()
//                        }
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

//                            _storesList.value = data.map { it.asDomainModel() }.toMutableList()
                            _storesList.value = data.elements.map { it.asDomainModel() }.toMutableList()
                        }

                    }
                }
            }
        }
    }

}