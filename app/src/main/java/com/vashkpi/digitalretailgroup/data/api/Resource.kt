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
    crossinline fetch : suspend () -> Response<T>
) = flow {

    Timber.i("loading")
    emit(Resource.Loading(null))

    try {
        Timber.i("emit1")
        emit(Resource.Success(fetch().body()))
    }
    catch(throwable : Throwable){
        Timber.i("emit2")
        emit(Resource.Error(throwable, null))
    }
}