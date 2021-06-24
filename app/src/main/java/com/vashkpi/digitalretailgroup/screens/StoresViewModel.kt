package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Store
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
class StoresViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    private val _storesList = MutableStateFlow(mutableListOf<Store>())
    val storesList: StateFlow<List<Store>> get() = _storesList

    fun obtainRegionStores(brandId: String, regionId: String) {
        viewModelScope.launch {
            apiRepository.getRegionStores(brandId, regionId).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.i("it's loading")
                        it.data?.let { data ->
                            Timber.d("here is the old data: $data")

                            _storesList.value = data.map { it.asDomainModel() }.toMutableList()
                        }
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

                            _storesList.value = data.map { it.asDomainModel() }.toMutableList()

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