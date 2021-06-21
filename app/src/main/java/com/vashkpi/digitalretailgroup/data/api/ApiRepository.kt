package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.*
import com.vashkpi.digitalretailgroup.data.models.network.*
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import kotlinx.coroutines.flow.Flow
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

    suspend fun getBalance(userId: String): Flow<Resource<out BalanceResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBalance(userId))
            },
            false
        )
    }

    suspend fun getSavePointsRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getSavePointsRules(userId))
            },
            false
        )
    }

    suspend fun getSpendPointsRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getSpendPointsRules(userId))
            },
            false
        )
    }

    suspend fun getPromotionRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getPromotionRules(userId))
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

    suspend fun getRegionStores(brandId: String, regionId: String): Flow<Resource<List<StoreEntity>>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                appDatabase.storeDao().getAll(brandId, regionId)
            },
            fetch = {
                ApiResponse.create(apiService.getRegionStores(brandId, regionId))
            },
            shouldFetch = {
                it.isEmpty()
            },
            saveFetchResult = {
                appDatabase.storeDao().insertMany(it.elements.map {
                    it.asDatabaseModel(brandId, regionId)
                })

            },
            mapper = {
                it.elements.map {
                    it.asDatabaseModel(brandId, regionId)
                }
            }
        )
    }

    suspend fun getStoreInfo(storeId: String): Flow<Resource<StoreInfoEntity?>> {
        Timber.d("trying")
        return networkBoundResource(
            query = {
                appDatabase.storeInfoDao().getOne(storeId)
            },
            fetch = {
                ApiResponse.create(apiService.getRegionStoreInfo(storeId))
            },
            shouldFetch = {
                it == null
            },
            saveFetchResult = {
                appDatabase.storeInfoDao().insertOne(it.asDatabaseModel(storeId))
            },
            mapper = {
                it.asDatabaseModel(storeId)
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