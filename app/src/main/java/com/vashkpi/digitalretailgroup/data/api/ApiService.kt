package com.vashkpi.digitalretailgroup.data.api

import com.google.gson.JsonElement
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.incoming.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.incoming.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
//    @POST("hs/bss/v1/register")
//    suspend fun registerPhone(
//        @Body registerPhone: RegisterPhone
//    ): Response<JsonElement>

    @POST("hs/bss/v1/register")
    suspend fun registerPhone(
        @Body registerPhone: RegisterPhone
    ): Response<ApiGenericAnswer>

    @POST("hs/bss/v1/confirm")
    suspend fun confirmCode(
        @Body confirmCode: ConfirmCode
    ): Response<ConfirmCodeResponse>

    @POST("hs/bss/v1/accounts")
    suspend fun saveProfileInfo(
        @Body accounts: Accounts
    ): Response<ApiGenericAnswer>
}