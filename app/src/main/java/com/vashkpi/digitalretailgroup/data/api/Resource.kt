package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}

/**
 * Will return the same throwable type but with different message
 */
fun Throwable.formatThrowableMessage(): Throwable {
    return when (this) {
        is java.net.UnknownHostException -> {
            java.net.UnknownHostException("Не удалось запросить данные от сервера (UnknownHost). Проверьте интернет соединение")
        }
        is java.net.SocketTimeoutException -> {
            java.net.SocketTimeoutException("Превышено время ожидания ответа от сервера")
        }
        else -> {
            this
        }
    }
}

inline fun <NetworkType> networkResponse(
    crossinline fetch : suspend () -> ApiResponse<NetworkType>,
    canBeEmptyResponse: Boolean,
    emitLoadingState: Boolean = true,
    crossinline onFetchSuccess: () -> Unit = { },
    crossinline onFetchFailed: () -> Unit = { }
) = flow {
    //Timber.d("loading")
    if (emitLoadingState) {
        emit(Resource.Loading(null))
    }
    try {
        val fetchResult: ApiResponse<NetworkType> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                onFetchSuccess()
                emit(Resource.Success(fetchResult.body))
                //currentCoroutineContext().cancel()
            }
            is ApiEmptyResponse -> {
                if (canBeEmptyResponse) {
                    onFetchSuccess()
                    emit(Resource.Success(null))
                    //currentCoroutineContext().cancel()
                }
                else {
                    onFetchFailed()
                    emit(
                        Resource.Error(
                            throwable = Throwable("Response is empty"),
                            null
                        )
                    )
                    //currentCoroutineContext().cancel()
                }
            }
            is ApiErrorResponse -> {
                onFetchFailed()
                emit(
                    Resource.Error(
                        throwable = Throwable("${fetchResult.errorCode}: ${fetchResult.errorMessage}"),
                        null
                    )
                )
                //currentCoroutineContext().cancel()
            }
        }
    } catch (throwable: Throwable) {
        Timber.d("some error processing networkResponse: $throwable")
        when {
            (canBeEmptyResponse && throwable is java.io.EOFException) -> {
                //it is made like that on the backend
                onFetchSuccess()
                emit(Resource.Success(null))
                //currentCoroutineContext().cancel()
            }
            else -> {
                onFetchFailed()

                val formattedThrowable = throwable.formatThrowableMessage()

                emit(Resource.Error(formattedThrowable, null))
                //currentCoroutineContext().cancel()
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
            val formattedThrowable = throwable.formatThrowableMessage()
            emit(Resource.Error(formattedThrowable, null))
        }

    }
    else {
        emit(Resource.Success(data))
    }
}
