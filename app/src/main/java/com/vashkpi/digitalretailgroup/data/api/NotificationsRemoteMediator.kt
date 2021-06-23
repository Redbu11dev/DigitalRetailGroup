package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity
import com.vashkpi.digitalretailgroup.data.models.network.asDatabaseModel
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NotificationsRemoteMediator(
    private val user_id: String,
    private val database: AppDatabase,
    private val networkService: ApiService
) : RemoteMediator<Int, NotificationEntity>() {
    private val notificationsDao = database.notificationDao()

    private var lastPageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NotificationEntity>
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            //no prepending
            return MediatorResult.Success(
                endOfPaginationReached = true
            )
        }
        else {
            return try {
                val response = if (loadType == LoadType.APPEND) {
                    networkResponse(
                        fetch = {
                            ApiResponse.create(networkService.getNotifications(user_id, lastPageIndex+1))
                        },
                        canBeEmptyResponse = false,
                        emitLoadingState = false
                    ).also {
                        lastPageIndex += 1
                    }
                }
                else {
                    lastPageIndex = 1
                    //networkService.getNotifications(user_id, lastPageIndex)
                    networkResponse(
                        fetch = {
                            ApiResponse.create(networkService.getNotifications(user_id, lastPageIndex))
                        },
                        canBeEmptyResponse = false,
                        emitLoadingState = false
                    )
                }.catch {

                }.first()

                val notifications = when (response) {
                    is Resource.Error -> {
                        throw response.error!!
                    }
                    is Resource.Loading -> {
                        //do nothing
                        throw IllegalStateException("Resource.Loading should be an impossible state")
                    }
                    is Resource.Success -> {
                        response.data!!.notifications.map { it.asDatabaseModel() }
                    }
                }

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        notificationsDao.clearAll()
                    }

                    // Insert new users into database, which invalidates the
                    // current PagingData, allowing Paging to present the updates
                    // in the DB.
                    notificationsDao.insertMany(notifications)
                }

                return MediatorResult.Success(
                    endOfPaginationReached = notifications.isEmpty()
                )
            }
            catch (t: Throwable) {
                // Handle other errors in this block and return MediatorResult.Error if it is an
                // unexpected error.
                MediatorResult.Error(t)
            }
        }
    }


}