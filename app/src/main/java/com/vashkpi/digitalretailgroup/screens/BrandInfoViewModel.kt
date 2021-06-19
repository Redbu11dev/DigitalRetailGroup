package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.Brand
import com.vashkpi.digitalretailgroup.data.models.BrandInfoRegion
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
class BrandInfoViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _website = MutableStateFlow("")
    val website: StateFlow<String> get() = _website

    private val _telephone = MutableStateFlow("")
    val telephone: StateFlow<String> get() = _telephone

    private val _timeOfWork = MutableStateFlow("")
    val timeOfWork: StateFlow<String> get() = _timeOfWork

    private val _brandRegionsList = MutableStateFlow(arrayListOf<BrandInfoRegion>())
    val brandRegionsList: StateFlow<ArrayList<BrandInfoRegion>> get() = _brandRegionsList

    fun getBrandInfo(brand: Brand) {
        viewModelScope.launch {
            apiRepository.getBrandInfo(brand.brand_id).collect {
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

                            _name.value = data.name
                            _website.value = data.website
                            _telephone.value = data.telephone
                            _timeOfWork.value = data.time_of_work
                            _brandRegionsList.value = data.regions

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