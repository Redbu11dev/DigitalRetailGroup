package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.mappers.Mapper
import com.vashkpi.digitalretailgroup.data.models.domain.*
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.network.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

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

    suspend fun getBrandInfo(brandId: String): Flow<Resource<out BrandInfo?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                //dummy for now
                flow<BrandInfo?> {
                    emit(null)
                }
            },
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(brandId))
            },
            mapper = {
                object : Mapper<BrandInfoResponse, BrandInfo> {
                    override fun map(input: BrandInfoResponse): BrandInfo {

                        val brandsRegionsArray = ArrayList<BrandInfoRegion>()
                        input.regions.forEach {
                            brandsRegionsArray.add(
                                BrandInfoRegion(
                                    it.name,
                                    it.region_id,
                                    it.order,
                                )
                            )
                        }

                        return BrandInfo(
                            input.name,
                            input.website,
                            input.telephone,
                            input.time_of_work,
                            brandsRegionsArray
                        )
                    }
                }.map(it)
            }
        )
    }

}