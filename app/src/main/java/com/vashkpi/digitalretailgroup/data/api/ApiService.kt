package com.vashkpi.digitalretailgroup.data.api

import com.google.gson.JsonElement
import com.vashkpi.digitalretailgroup.data.models.outgoing.registerPhone
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun registerPhone(
        @Body registerPhone: registerPhone
    ): Response<JsonElement>
}