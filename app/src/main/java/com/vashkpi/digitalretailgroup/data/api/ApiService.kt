package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.response.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.response.BrandsResponse
import com.vashkpi.digitalretailgroup.data.models.response.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import com.vashkpi.digitalretailgroup.data.models.response.BrandInfoResponse
import retrofit2.Response
import retrofit2.http.*

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

    @GET("hs/bss/v1/brands")
    suspend fun getBrands(
    ): Response<BrandsResponse>

    @GET("hs/bss/v1/brands/{brand_id}")
    suspend fun getBrandInfo(
        //@Header("brand_id") brandIdHeader: String,
        @Path(value = "brand_id") brandId: String
    ): Response<BrandInfoResponse>
}