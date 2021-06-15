package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}

inline fun <T> networkBoundResource(
    crossinline fetch : suspend () -> ApiResponse<T>
) = flow {
    //Timber.i("loading")
    emit(Resource.Loading(null))

    try {
        //Timber.i("emit1")
        //TODO here check if cached and cache if needed
        val fetchResult: ApiResponse<T> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                //TODO()
                emit(Resource.Success(fetchResult.body))
            }
            is ApiEmptyResponse -> {
                //TODO()
                //emit(Resource.Success<T>())
            }
            is ApiErrorResponse -> {
                //TODO()
                emit(Resource.Error(throwable = Throwable("${fetchResult.errorCode}: ${fetchResult.errorMessage}"), null))
            }
        }

//        if (fetchResult.isSuccessful) {
//            emit(Resource.Success(fetchResult.body()))
//        }
//        else {
//            emit(Resource.Error(throwable = Throwable("hggh"), null))
//        }
    }
    catch(throwable : Throwable){
        //Timber.i("emit2")
        emit(Resource.Error(throwable, null))
    }
}