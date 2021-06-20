package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.*
import com.vashkpi.digitalretailgroup.data.models.network.*
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
                appDatabase.brandDao().getAll()
            },
            fetch = {
                ApiResponse.create(apiService.getBrands())
            },
            shouldFetch = {
                it.isEmpty()
            },
            saveFetchResult = {
                appDatabase.brandDao().insertMany(it.elements.map {
                    it.asDatabaseModel()
                })

            },
            mapper = {
                it.elements.map {
                    it.asDatabaseModel()
                }
            }
        )
    }

    suspend fun getBrandInfo(brandId: String): Flow<Resource<BrandInfoEntityFull?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                    //flowOf<BrandInfoEntityFull?>(null)
                appDatabase.brandInfoDao().getOne(brandId)
            },
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(brandId))
            },
            shouldFetch = {
                it == null
            },
            saveFetchResult = {
                appDatabase.brandInfoDao().insertBrandInfoEntity(
                    it.asDatabaseModel(brandId).brandInfoEntity,
                    it.asDatabaseModel(brandId).regions
                )
            },
            mapper = {
                it.asDatabaseModel(brandId)
            }
        )
    }

    suspend fun getNotifications(userId: String, page: Int): Flow<Resource<List<NotificationEntity>>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                appDatabase.notificationDao().getAll()
            },
            fetch = {
                ApiResponse.create(apiService.getNotifications(userId, page))
            },
            shouldFetch = {
                it.isEmpty()
            },
            saveFetchResult = {
                appDatabase.notificationDao().insertMany(it.notifications.map {
                    it.asDatabaseModel()
                })
            },
            mapper = {
                it.notifications.map {
                    it.asDatabaseModel()
                }
            }
        )
    }

}