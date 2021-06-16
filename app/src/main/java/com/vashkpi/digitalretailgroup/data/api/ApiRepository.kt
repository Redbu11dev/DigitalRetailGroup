package com.vashkpi.digitalretailgroup.data.api

import android.content.Context
import com.vashkpi.digitalretailgroup.data.models.incoming.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(appContext: Context, private val  apiService: ApiService) {

//    suspend fun registerPhone(registerPhone: RegisterPhone): ApiResponse {
//        return try {
//            val response = apiService.registerPhone(registerPhone)
//            //println("response error body api: ${response.errorBody()?.string()}")
//
//            //networkBoundResource(suspend {apiService.registerPhone(registerPhone)})
//
//            ApiResponse(null, response)
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            ApiResponse(e, null)
//        }
//    }

    suspend fun registerPhone(registerPhone: RegisterPhone): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.i("trying")
        return networkBoundResource(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhone))
            }
        )
    }

//    suspend fun confirmCode(confirmCode: ConfirmCode): ApiResponse {
//        return try {
//            val response = apiService.confirmCode(confirmCode)
//            //println("response error body api: ${response.errorBody()?.string()}")
//            ApiResponse(null, response)
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            ApiResponse(e, null)
//        }
//    }
}