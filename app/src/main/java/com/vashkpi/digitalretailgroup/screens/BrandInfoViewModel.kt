package com.vashkpi.digitalretailgroup.screens

import androidx.lifecycle.viewModelScope
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.Resource
import com.vashkpi.digitalretailgroup.data.models.database.asDomainModel
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfo
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion
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
class BrandInfoViewModel @Inject constructor(private val apiRepository: ApiRepository): BaseViewModel() {

//    private val _name = MutableStateFlow("")
//    val name: StateFlow<String> get() = _name
//
//    private val _website = MutableStateFlow("")
//    val website: StateFlow<String> get() = _website
//
//    private val _telephone = MutableStateFlow("")
//    val telephone: StateFlow<String> get() = _telephone
//
//    private val _timeOfWork = MutableStateFlow("")
//    val timeOfWork: StateFlow<String> get() = _timeOfWork
//
//    private val _brandRegionsList = MutableStateFlow(mutableListOf<BrandInfoRegion>())
//    val brandRegionsList: StateFlow<List<BrandInfoRegion>> get() = _brandRegionsList

    private val _brandInfo = MutableStateFlow<BrandInfo?>(null)
    val brandInfo: StateFlow<BrandInfo?> get() = _brandInfo

//    init {
//        getBrandInfo()
//    }

    fun getBrandInfo(brand: Brand) {
        viewModelScope.launch {
            apiRepository.getBrandInfo(brand.brand_id).collect {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("it's loading")
//                        it.data?.let { data ->
//                            Timber.d("here is the old data: $data")
//
//                            _brandInfo.value = data.asDomainModel()
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

                            _brandInfo.value = data.asDomainModel()

//                            _name.value = domainModel.name
//                            _website.value = domainModel.website
//                            _telephone.value = domainModel.telephone
//                            _timeOfWork.value = domainModel.time_of_work
//                            _brandRegionsList.value = domainModel.regions.toMutableList()


                        }
                    }
                }
            }
        }
    }

}