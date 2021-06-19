package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.mappers.Mapper
import com.vashkpi.digitalretailgroup.data.models.response.GenericResponse
import com.vashkpi.digitalretailgroup.data.models.response.BrandsResponse
import com.vashkpi.digitalretailgroup.data.models.response.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.Accounts
import com.vashkpi.digitalretailgroup.data.models.Brand
import com.vashkpi.digitalretailgroup.data.models.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.RegisterPhone
import com.vashkpi.digitalretailgroup.data.models.response.BrandInfoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    suspend fun getBrands(): Flow<Resource<ArrayList<Brand>?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                //dummy for now
                flow<ArrayList<Brand>> {
                    emit(ArrayList<Brand>())
                }
            },
            fetch = {
                ApiResponse.create(apiService.getBrands())
            },
            mapper = {
                object : Mapper<BrandsResponse, ArrayList<Brand>> {
                    override fun map(input: BrandsResponse): ArrayList<Brand> {
                        val brandsArray = ArrayList<Brand>()
                        input.elements.forEach {
                            brandsArray.add(
                                Brand(
                                it.name,
                                it.brand_id,
                                it.image_parth,
                                it.order,
                            )
                            )
                        }
                        return brandsArray
                    }
                }.map(it)
            }
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