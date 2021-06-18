package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.response.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.response.BrandsResponse
import com.vashkpi.digitalretailgroup.data.models.response.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import com.vashkpi.digitalretailgroup.data.models.response.BrandInfoResponse
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val  apiService: ApiService) {

    suspend fun registerPhone(registerPhone: RegisterPhone): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return simpleNetworkResponse(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhone))
            }
        )
    }

    suspend fun confirmCode(confirmCode: ConfirmCode): Flow<Resource<out ConfirmCodeResponse?>> {
        Timber.d("trying")
        return simpleNetworkResponse(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCode))
            }
        )
    }

    suspend fun saveProfileInfo(accounts: Accounts): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return simpleNetworkResponse(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accounts))
            }
        )
    }

    suspend fun getBrands(): Flow<Resource<out BrandsResponse?>> {
        Timber.d("trying")
        return simpleNetworkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrands())
            },
            true
        )
    }

    suspend fun getBrandInfo(brandId: String): Flow<Resource<out BrandInfoResponse?>> {
        Timber.d("trying")
        return simpleNetworkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(brandId))
            }
        )
    }

}