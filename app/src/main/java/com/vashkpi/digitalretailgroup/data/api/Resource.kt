package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}

inline fun <NetworkType> networkResponse(
    crossinline fetch : suspend () -> ApiResponse<NetworkType>,
    canBeEmptyResponse: Boolean = false
) = flow {
    //Timber.d("loading")
    emit(Resource.Loading(null))
    try {
        //Timber.d("emit1")
        //TODO here check if cached and cache if needed
        val fetchResult: ApiResponse<NetworkType> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                emit(Resource.Success(fetchResult.body))
            }
            is ApiEmptyResponse -> {
                if (canBeEmptyResponse) {
                    emit(Resource.Success(null))
                }
                else {
                    emit(
                        Resource.Error(
                            throwable = Throwable("Response is empty"),
                            null
                        )
                    )
                }
            }
            is ApiErrorResponse -> {
                emit(
                    Resource.Error(
                        throwable = Throwable("${fetchResult.errorCode}: ${fetchResult.errorMessage}"),
                        null
                    )
                )
            }
        }
    } catch (throwable: Throwable) {
        //Timber.d("in here")
        emit(Resource.Error(throwable, null))
    }
}

inline fun <NetworkType, DatabaseType> networkBoundResource(
    //query
    crossinline query: () -> Flow<DatabaseType>,
    crossinline fetch : suspend () -> ApiResponse<NetworkType>,
    //shouldFetch / boolean
    //saveFetchResult?!
    crossinline mapper: (NetworkType) -> DatabaseType
) = flow<Resource<DatabaseType>> {
    //Timber.d("loading")
    emit(Resource.Loading(null))
    try {
        //Timber.d("emit1")
        //TODO here check if cached and cache if needed
        val fetchResult: ApiResponse<NetworkType> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                emit(Resource.Success(mapper.invoke(fetchResult.body)))
            }
            is ApiEmptyResponse -> {
                emit(
                    Resource.Error(
                        throwable = Throwable("Response is empty"),
                        null
                    )
                )
            }
            is ApiErrorResponse -> {
                emit(
                    Resource.Error(
                        throwable = Throwable("${fetchResult.errorCode}: ${fetchResult.errorMessage}"),
                        null
                    )
                )
            }
        }
    } catch (throwable: Throwable) {
        //Timber.d("in here")
        emit(Resource.Error(throwable, null))
    }
}
