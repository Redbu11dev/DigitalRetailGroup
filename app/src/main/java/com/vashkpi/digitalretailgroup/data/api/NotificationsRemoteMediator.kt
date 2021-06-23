package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vashkpi.digitalretailgroup.data.database.AppDatabase
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity
import com.vashkpi.digitalretailgroup.data.models.network.asDatabaseModel
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
        return try {
            when (loadType) {
                LoadType.PREPEND -> {
                    //no prepending
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                    val response = networkService.getNotifications(user_id, lastPageIndex+1) //take as networkResponse
                    lastPageIndex += 1

                    database.withTransaction {
                        // Insert new users into database, which invalidates the
                        // current PagingData, allowing Paging to present the updates
                        // in the DB.
                        notificationsDao.insertMany(response.body()!!.notifications.map { it.asDatabaseModel() })
                    }

                    return MediatorResult.Success(
                        endOfPaginationReached = response.body()!!.notifications.isEmpty()
                    )
                }
                LoadType.REFRESH -> {
                    lastPageIndex = 1
                    val response = networkService.getNotifications(user_id, lastPageIndex)

                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            notificationsDao.clearAll()
                        }

                        // Insert new users into database, which invalidates the
                        // current PagingData, allowing Paging to present the updates
                        // in the DB.
                        notificationsDao.insertMany(response.body()!!.notifications.map { it.asDatabaseModel() })
                    }

                    return MediatorResult.Success(
                        endOfPaginationReached = response.body()!!.notifications.isEmpty()
                    )
                }
            }
        }
        catch (e: IOException) {
            // IOException for network failures.
            MediatorResult.Error(e)
        }
        catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            MediatorResult.Error(e)
        }
        catch (t: Throwable) {
            // Handle other errors in this block and return MediatorResult.Error if it is an
            // unexpected error.
            MediatorResult.Error(t)
        }
    }


}