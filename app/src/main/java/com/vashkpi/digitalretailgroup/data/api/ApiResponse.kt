package com.vashkpi.digitalretailgroup.data.api

import com.google.gson.JsonElement
import retrofit2.Response

data class ApiResponse(
    val error: Throwable? = Throwable("Unknown error"),
    val response: Response<JsonElement>?
)