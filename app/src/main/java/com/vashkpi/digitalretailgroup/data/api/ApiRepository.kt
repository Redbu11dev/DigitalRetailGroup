package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.*
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.*
import com.vashkpi.digitalretailgroup.data.models.network.*
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase, private val dataStoreRepository: DataStoreRepository) {

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

    suspend fun getProfileInfo(userId: String): Flow<Resource<out AccountsGetResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getProfileInfo(userId))
            },
            false
        )
//        return networkBoundResource(
//            query = {
//                //appDatabase.brandDao().getAll()
//                    flow {
//                        emit(dataStoreRepository.userInfo)
//                    }
//            },
//            fetch = {
//                ApiResponse.create(apiService.getProfileInfo(userId))
//            },
//            shouldFetch = {
//                //it == null
//                true
//            },
//            saveFetchResult = {
//                dataStoreRepository.userInfo = it.user_info.asDomainModel()
//            },
//            mapper = {
//                it.user_info.asDomainModel()
//            }
//        )
    }

    suspend fun saveDeviceInfo(devicesDto: DevicesDto): Flow<Resource<out GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveDeviceInfo(devicesDto))
            },
            true
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

    suspend fun getCode(userId: String): Flow<Resource<out CodeResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getCode(userId))
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
                //it.isEmpty()
                true
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

//    suspend fun getNotifications(userId: String, page: Int): Flow<Resource<List<NotificationEntity>>> {
//        Timber.d("trying")
//        return networkBoundResource(
//            query = {
//                appDatabase.notificationDao().getAll()
//            },
//            fetch = {
//                ApiResponse.create(apiService.getNotifications(userId, page))
//            },
//            shouldFetch = {
//                //it.isEmpty()
//                true
//            },
//            saveFetchResult = {
//                appDatabase.notificationDao().insertMany(it.notifications.map {
//                    it.asDatabaseModel()
//                })
//            },
//            mapper = {
//                it.notifications.map {
//                    it.asDatabaseModel()
//                }
//            }
//        )
//    }

    @ExperimentalPagingApi
    fun getNotifications(): Flow<PagingData<NotificationEntity>> {
        Timber.d("trying")
        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            PagingConfig(pageSize = 10, initialLoadSize = 10),
            remoteMediator = NotificationsRemoteMediator(dataStoreRepository.userId, appDatabase, apiService)
        ) {
            //NotificationsPagingSource(apiService, dataStoreRepository.userId)
            appDatabase.notificationDao().pagingSourceOfNotRemoved()

        }.flow
    }

    suspend fun syncNotifications() {
        Timber.d("syncNotifications called")
        //(sync local changes with the server)
        //try to fan-out deletion requests for locally removed notifications
        //try to fan-out "read" requests for locally "read" notifications
        appDatabase.notificationDao().getAllUserModified().first().forEach {
            if (it.local_user_removed) {
                Timber.d("syncNotifications called1")
                deleteNotificationRemotely(dataStoreRepository.userId, it.notification_id).collect {

                }
            }
            else if (!it.local_user_removed && it.local_user_read) {
                Timber.d("syncNotifications called2")
                markNotificationRead(NotificationPostDto(dataStoreRepository.userId, it.notification_id)).collect {

                }
            }
        }
    }

    suspend fun markNotificationRead(notificationPostDto: NotificationPostDto): Flow<Resource<out GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.markNotificationRead(notificationPostDto))
            },
            true
        ).also {
            appDatabase.notificationDao().setRead(notificationPostDto.notification_id, true)
        }
    }

    suspend fun deleteNotificationLocally(notificationId: String) {
        Timber.d("trying")
        appDatabase.notificationDao().markUserRemoved(notificationId, true)
    }

    suspend fun restoreNotificationLocally(notificationId: String) {
        Timber.d("trying")
        appDatabase.notificationDao().markUserRemoved(notificationId, false)
    }

    suspend fun deleteNotificationRemotely(userId: String, notificationId: String): Flow<Resource<out GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.deleteNotification(userId, notificationId))
            },
            true,
            onFetchSuccess = {
                runBlocking {
                    appDatabase.notificationDao().deleteOne(notificationId)
                }
            }
        )
    }

}