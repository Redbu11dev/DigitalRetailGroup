package com.vashkpi.digitalretailgroup.data.api

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.vashkpi.digitalretailgroup.data.models.network.GenericResponseDto
import retrofit2.Response
import timber.log.Timber

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            //Timber.d("body.toString()1: ${response.body().toString()}")
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                var message = try {
                    Gson().fromJson(response.errorBody()?.string(), GenericResponseDto::class.java).message
                }
                catch (t: Throwable) {
                    response.errorBody()?.string() ?: response.message()
                }
                if (message.isEmpty()) {
                    message = "Unexpected error"
                }
                //val message = response.errorBody()?.string() ?: response.message()
                ApiErrorResponse(response.code(), message)
            }
        }

//        fun <T> create(errorCode: Int, error: Throwable): ApiErrorResponse<T> {
//            return ApiErrorResponse(errorCode, error.message ?: "Unknown Error!")
//        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()
data class ApiErrorResponse<T>(val errorCode: Int, val errorMessage: String): ApiResponse<T>()
data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()