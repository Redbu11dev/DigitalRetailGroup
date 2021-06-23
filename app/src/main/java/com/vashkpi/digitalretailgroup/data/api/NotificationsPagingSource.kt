package com.vashkpi.digitalretailgroup.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vashkpi.digitalretailgroup.data.models.network.NotificationDto
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import retrofit2.HttpException
import java.io.IOException

/**
 * unused, went with RemoteMediator
 */
class NotificationsPagingSource(
    private val apiService: ApiService,
    private val userId: String
) : PagingSource<Int, NotificationDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationDto> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = apiService.getNotifications(userId, nextPageNumber)

            return LoadResult.Page(
                data = response.body()!!.notifications,
                prevKey = null, // Only paging forward.
                nextKey = response.body()!!.page_next.toInt()
            )
        }
        catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        }
        catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
        catch (t: Throwable) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationDto>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}