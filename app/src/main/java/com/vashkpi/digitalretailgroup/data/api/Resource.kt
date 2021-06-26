package com.vashkpi.digitalretailgroup.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

sealed class Resource<out T> {
    data class Success<T>(val data: T? = null) : Resource<T>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
    data class Error<T>(val error: Throwable, val data: T? = null) : Resource<T>()
}

/**
 * Will return the same throwable type but with different message
 */
fun Throwable.formatThrowableMessage(): Throwable {
    return when (this) {
        is java.net.UnknownHostException -> {
            java.net.UnknownHostException("Не удалось подключиться к серверу (UnknownHost). Проверьте интернет соединение")
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
): Flow<Resource<NetworkType?>> = flow {
    //Timber.d("loading")
    if (emitLoadingState) {
        Timber.d("it's loading")
        emit(Resource.Loading())
    }
        val fetchResult: ApiResponse<NetworkType> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                onFetchSuccess()
                Timber.d("it's success")
                emit(Resource.Success(fetchResult.body))
                currentCoroutineContext().cancel()
            }
            is ApiEmptyResponse -> {
                onFetchSuccess()
                Timber.d("it's success")
                emit(Resource.Success(null))
                currentCoroutineContext().cancel()
            }
            is ApiErrorResponse -> {
                onFetchFailed()
                val errorMessage = "${fetchResult.errorCode}: ${fetchResult.errorMessage}"
                Timber.d("it's error: ${errorMessage}")
                emit(
                    Resource.Error(
                        error = Throwable(errorMessage),
                        null
                    )
                )
                currentCoroutineContext().cancel()
            }
        }
}.catch {
    val throwable = it
    Timber.d("some error processing networkResponse: $throwable")
    onFetchFailed()
    val formattedThrowable = throwable.formatThrowableMessage()
    Timber.d("it's error: ${formattedThrowable.message}")
    emit(Resource.Error(formattedThrowable, null))
    currentCoroutineContext().cancel()
}

inline fun <NetworkType, DatabaseType> networkBoundResource(
    //query
    crossinline query: () -> Flow<DatabaseType>,
    crossinline fetch : suspend () -> ApiResponse<NetworkType>,
    crossinline shouldFetch: (DatabaseType) -> Boolean,
    crossinline saveFetchResult: suspend (NetworkType) -> Unit,
    crossinline mapper: (NetworkType) -> DatabaseType
): Flow<Resource<DatabaseType>> = flow {
    val data = query().first()

    if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        //Timber.d("emit1")
        val fetchResult: ApiResponse<NetworkType> = fetch()

        when (fetchResult) {
            is ApiSuccessResponse -> {
                saveFetchResult(fetchResult.body)
                emit(Resource.Success(mapper.invoke(fetchResult.body)))
                currentCoroutineContext().cancel()
            }
            is ApiEmptyResponse -> {
                emit(
                    Resource.Error(
                        error = Throwable("Response is empty"),
                        null
                    )
                )
                currentCoroutineContext().cancel()
            }
            is ApiErrorResponse -> {
                emit(
                    Resource.Error(
                        error = Throwable("${fetchResult.errorCode}: ${fetchResult.errorMessage}"),
                        null
                    )
                )
                currentCoroutineContext().cancel()
            }
        }

    }
    else {
        emit(Resource.Success(data))
    }
}.catch {
    val throwable = it
    Timber.d("some error processing networkBoundResource: $throwable")
    val formattedThrowable = throwable.formatThrowableMessage()
    emit(Resource.Error(formattedThrowable, null))
    currentCoroutineContext().cancel()
}
