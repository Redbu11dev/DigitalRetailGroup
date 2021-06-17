package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.flow.flow

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}

inline fun <T> simpleNetworkResource(
    crossinline fetch : suspend () -> ApiResponse<T>
) = flow {
    //Timber.d("loading")
    emit(Resource.Loading(null))

    try {
        //Timber.d("emit1")
        //TODO here check if cached and cache if needed
        val fetchResult: ApiResponse<T> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                emit(Resource.Success(fetchResult.body))
            }
            is ApiEmptyResponse -> {
                emit(Resource.Success(null))
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
