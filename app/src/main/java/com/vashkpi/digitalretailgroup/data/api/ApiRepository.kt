package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import com.vashkpi.digitalretailgroup.data.models.network.*
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase) {

    suspend fun registerPhone(registerPhoneDto: RegisterPhoneDto): Flow<Resource<out GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhoneDto))
            },
            false
        )
    }

    suspend fun confirmCode(confirmCodeDto: ConfirmCodeDto): Flow<Resource<out ConfirmCodeResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCodeDto))
            },
            false
        )
    }

    suspend fun saveProfileInfo(accountsDto: AccountsDto): Flow<Resource<out GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accountsDto))
            },
            false
        )
    }

    suspend fun getBrands(): Flow<Resource<List<BrandEntity>>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                appDatabase.brandDao().getBrands()
            },
            fetch = {
                ApiResponse.create(apiService.getBrands())
            },
            shouldFetch = {
                it.isEmpty()
            },
            saveFetchResult = {
                appDatabase.brandDao().insertBrands(it.asDatabaseModel())

            },
            mapper = {
                it.asDatabaseModel()
            }
        )
    }

    suspend fun getBrandInfo(brandId: String): Flow<Resource<BrandInfoEntity?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                //dummy for now
                flow<BrandInfoEntity?> {
                    emit(null)
                }
            },
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(brandId))
            },
            shouldFetch = {
                true
            },
            saveFetchResult = {

            },
            mapper = {
                it.asDatabaseModel()
            }
        )
    }

}