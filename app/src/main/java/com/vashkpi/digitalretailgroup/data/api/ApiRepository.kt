package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.response.GenericResponse
import com.vashkpi.digitalretailgroup.data.models.response.BrandsResponse
import com.vashkpi.digitalretailgroup.data.models.response.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.Accounts
import com.vashkpi.digitalretailgroup.data.models.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.RegisterPhone
import com.vashkpi.digitalretailgroup.data.models.response.BrandInfoResponse
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val  apiService: ApiService) {

    suspend fun registerPhone(registerPhone: RegisterPhone): Flow<Resource<out GenericResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhone))
            }
        )
    }

    suspend fun confirmCode(confirmCode: ConfirmCode): Flow<Resource<out ConfirmCodeResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCode))
            }
        )
    }

    suspend fun saveProfileInfo(accounts: Accounts): Flow<Resource<out GenericResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accounts))
            }
        )
    }

    suspend fun getBrands(): Flow<Resource<out BrandsResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrands())
            },
            true
        )
    }

    suspend fun getBrandInfo(brandId: String): Flow<Resource<out BrandInfoResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(brandId))
            }
        )
    }

}