package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.flow.*
import timber.log.Timber

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
    canBeEmptyResponse: Boolean
) = flow {
    //Timber.d("loading")
    emit(Resource.Loading(null))
    try {
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
        Timber.d("some error processing networkResponse: $throwable")
        when {
            (canBeEmptyResponse && throwable is java.io.EOFException) -> {
                //it is made like that on the backend
                emit(Resource.Success(null))
            }
            else -> {
                emit(Resource.Error(throwable, null))
            }
        }
    }
}

inline fun <NetworkType, DatabaseType> networkBoundResource(
    //query
    crossinline query: () -> Flow<DatabaseType>,
    crossinline fetch : suspend () -> ApiResponse<NetworkType>,
    crossinline shouldFetch: (DatabaseType) -> Boolean,
    crossinline saveFetchResult: suspend (NetworkType) -> Unit,
    crossinline mapper: (NetworkType) -> DatabaseType
) = flow<Resource<DatabaseType>> {
    //Timber.d("loading")
    //emit(Resource.Loading(null))

    val data = query().first()

    if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            //Timber.d("emit1")
            val fetchResult: ApiResponse<NetworkType> = fetch()

            when (fetchResult) {
                is ApiSuccessResponse -> {
                    saveFetchResult(fetchResult.body)
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
    else {
        emit(Resource.Success(data))
    }
}
