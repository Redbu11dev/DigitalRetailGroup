package com.vashkpi.digitalretailgroup.data.api

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

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun registerPhone(registerPhoneDto: RegisterPhoneDto): Flow<Resource<out GenericResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhoneDto))
            }
        )
    }

    suspend fun confirmCode(confirmCodeDto: ConfirmCodeDto): Flow<Resource<out ConfirmCodeResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCodeDto))
            }
        )
    }

    suspend fun saveProfileInfo(accountsDto: AccountsDto): Flow<Resource<out GenericResponse?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accountsDto))
            }
        )
    }

    suspend fun getBrands(): Flow<Resource<List<BrandEntity>?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                //dummy for now
                flow<List<BrandEntity>> {
                    emit(mutableListOf<BrandEntity>())
                }
            },
            fetch = {
                ApiResponse.create(apiService.getBrands())
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
            mapper = {
                it.asDatabaseModel()
            }
        )
    }

}