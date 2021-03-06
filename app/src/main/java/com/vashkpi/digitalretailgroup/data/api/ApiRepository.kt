package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.*
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.*
import com.vashkpi.digitalretailgroup.data.models.network.*
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase, private val dataStoreRepository: DataStoreRepository) {

    suspend fun registerPhone(registerPhoneDto: RegisterPhoneDto): Flow<Resource<GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhoneDto))
            }
        )
    }

    suspend fun confirmCode(confirmCodeDto: ConfirmCodeDto): Flow<Resource<ConfirmCodeResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCodeDto))
            }
        )
    }

    suspend fun saveProfileInfo(accountsDto: AccountsDto): Flow<Resource<GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accountsDto))
            }
        )
    }

    suspend fun getProfileInfo(userId: String): Flow<Resource<AccountsGetResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getProfileInfo(userId))
            }
        )
    }

    suspend fun saveDeviceInfo(devicesDto: DevicesDto): Flow<Resource<GenericResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.saveDeviceInfo(devicesDto))
            }
        )
    }

    suspend fun getBalance(userId: String): Flow<Resource<BalanceResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBalance(userId))
            }
        )
    }

    suspend fun getCode(userId: String): Flow<Resource<CodeResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getCode(userId))
            }
        )
    }

    suspend fun getSavePointsRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getSavePointsRules(userId))
            }
        )
    }

    suspend fun getSpendPointsRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getSpendPointsRules(userId))
            }
        )
    }

    suspend fun getPromotionRules(userId: String): Flow<Resource<out RulesResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getPromotionRules(userId))
            }
        )
    }

    suspend fun getBrands(): Flow<Resource<BrandsResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrands(dataStoreRepository.userId))
            }
        )
//        return networkBoundResource(
//            query = {
//                appDatabase.brandDao().getAll()
//            },
//            fetch = {
//                ApiResponse.create(apiService.getBrands(dataStoreRepository.userId))
//            },
//            shouldFetch = {
//                //it.isEmpty()
//                true
//            },
//            saveFetchResult = {
//                appDatabase.brandDao().clearAll()
//                appDatabase.brandDao().insertMany(it.elements.map {
//                    it.asDatabaseModel()
//                })
//
//            },
//            mapper = {
//                it.elements.map {
//                    it.asDatabaseModel()
//                }
//            }
//        )
    }

    suspend fun getBrandInfo(brandId: String): Flow<Resource<BrandInfoResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getBrandInfo(dataStoreRepository.userId, brandId))
            }
        )
//        return networkBoundResource(
//            query = {
//                    //flowOf<BrandInfoEntityFull?>(null)
//                appDatabase.brandInfoDao().getOne(brandId)
//            },
//            fetch = {
//                ApiResponse.create(apiService.getBrandInfo(dataStoreRepository.userId, brandId))
//            },
//            shouldFetch = {
//                //it == null
//                true
//            },
//            saveFetchResult = {
//                appDatabase.brandInfoDao().clearAll()
//                appDatabase.brandInfoDao().insertBrandInfoEntity(
//                    it.asDatabaseModel(brandId).brandInfoEntity,
//                    it.asDatabaseModel(brandId).regions
//                )
//            },
//            mapper = {
//                it.asDatabaseModel(brandId)
//            }
//        )
    }

    suspend fun getRegionStores(brandId: String, regionId: String): Flow<Resource<StoresResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getRegionStores(dataStoreRepository.userId, brandId, regionId))
            }
        )
//        return networkBoundResource(
//            query = {
//                appDatabase.storeDao().getAll(brandId, regionId)
//            },
//            fetch = {
//                ApiResponse.create(apiService.getRegionStores(dataStoreRepository.userId, brandId, regionId))
//            },
//            shouldFetch = {
//                //it.isEmpty()
//                true
//            },
//            saveFetchResult = {
//                appDatabase.storeDao().insertMany(it.elements.map {
//                    it.asDatabaseModel(brandId, regionId)
//                })
//
//            },
//            mapper = {
//                it.elements.map {
//                    it.asDatabaseModel(brandId, regionId)
//                }
//            }
//        )
    }

    suspend fun getStoreInfo(storeId: String): Flow<Resource<StoreInfoResponseDto?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.getRegionStoreInfo(dataStoreRepository.userId, storeId))
            }
        )
//        return networkBoundResource(
//            query = {
//                appDatabase.storeInfoDao().getOne(storeId)
//            },
//            fetch = {
//                ApiResponse.create(apiService.getRegionStoreInfo(dataStoreRepository.userId, storeId))
//            },
//            shouldFetch = {
//                //it == null
//                true
//            },
//            saveFetchResult = {
//                appDatabase.storeInfoDao().insertOne(it.asDatabaseModel(storeId))
//            },
//            mapper = {
//                it.asDatabaseModel(storeId)
//            }
//        )
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
        val list = appDatabase.notificationDao().getAllUserModified().first()
        Timber.d("syncNotifications size: ${list.size}")
        list.forEachIndexed { i: Int, notificationEntity: NotificationEntity ->

            if (notificationEntity.local_user_removed) {
                Timber.d("syncNotifications called $i - 1")
                deleteNotificationRemotely(
                    dataStoreRepository.userId,
                    notificationEntity.notification_id
                ).collect {

                }
            } else if (!notificationEntity.local_user_removed && notificationEntity.local_user_read) {
                Timber.d("syncNotifications called $i - 2")
                markNotificationRead(
                    NotificationPostDto(
                        dataStoreRepository.userId,
                        notificationEntity.notification_id
                    )
                ).collect {

                }
            }

        }
    }

    suspend fun markNotificationRead(notificationPostDto: NotificationPostDto): Flow<Resource<Unit?>> {
        Timber.d("trying")
        return networkResponse(
            fetch = {
                ApiResponse.create(apiService.markNotificationRead(notificationPostDto))
            },
            true,
            onFetchSuccess = {
                //mark local read as false?
                runBlocking {
                    appDatabase.notificationDao().setRead(notificationPostDto.notification_id,
                        read = true,
                        local_user_read = false
                    )
                }
            },
            onFetchFailed = {
                runBlocking {
                    appDatabase.notificationDao().setRead(notificationPostDto.notification_id,
                        read = true,
                        local_user_read = true
                    )
                }
            }
        )
    }

    suspend fun deleteNotificationLocally(notificationId: String) {
        Timber.d("trying")
        appDatabase.notificationDao().markUserRemoved(notificationId, true)
    }

    suspend fun restoreNotificationLocally(notificationId: String) {
        Timber.d("trying")
        appDatabase.notificationDao().markUserRemoved(notificationId, false)
    }

    suspend fun deleteNotificationRemotely(userId: String, notificationId: String): Flow<Resource<Unit?>> {
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