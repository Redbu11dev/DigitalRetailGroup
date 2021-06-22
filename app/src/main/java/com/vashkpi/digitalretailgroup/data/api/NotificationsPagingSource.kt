package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vashkpi.digitalretailgroup.data.models.network.NotificationDto
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
//const val NETWORK_PAGE_SIZE = 10

class NotificationsPagingSource(
    private val apiService: ApiService,
    private val dataStoreRepository: DataStoreRepository
) : PagingSource<Int, NotificationDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationDto> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiService.getNotifications(
                userId = dataStoreRepository.userId,
                page = pageIndex
            )
            val notifications = response.body()!!.notifications.reversed()
            val nextKey =
                if (notifications.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = notifications,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, NotificationDto>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}