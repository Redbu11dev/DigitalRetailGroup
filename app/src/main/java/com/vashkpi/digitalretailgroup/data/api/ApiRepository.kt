package com.vashkpi.digitalretailgroup.data.api

import android.content.Context
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import javax.inject.Inject

class ApiRepository @Inject constructor(appContext: Context, private val  apiService: ApiService) {
    /**
     * Login
     */
    suspend fun registerPhone(registerPhone: RegisterPhone): ApiResponse {
        return try {
            val response = apiService.registerPhone(registerPhone)
            //println("response error body api: ${response.errorBody()?.string()}")
            ApiResponse(null, response)
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResponse(e, null)
        }
    }
}