package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.network.GenericResponse
import com.vashkpi.digitalretailgroup.data.models.network.BrandsResponse
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.network.AccountsDto
import com.vashkpi.digitalretailgroup.data.models.network.ConfirmCodeDto
import com.vashkpi.digitalretailgroup.data.models.network.RegisterPhoneDto
import com.vashkpi.digitalretailgroup.data.models.network.BrandInfoResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
//    @POST("hs/bss/v1/register")
//    suspend fun registerPhone(
//        @Body registerPhone: RegisterPhone
//    ): Response<JsonElement>

    @POST("hs/bss/v1/register")
    suspend fun registerPhone(
        @Body registerPhoneDto: RegisterPhoneDto
    ): Response<GenericResponse>

    @POST("hs/bss/v1/confirm")
    suspend fun confirmCode(
        @Body confirmCodeDto: ConfirmCodeDto
    ): Response<ConfirmCodeResponse>

    @POST("hs/bss/v1/accounts")
    suspend fun saveProfileInfo(
        @Body accountsDto: AccountsDto
    ): Response<GenericResponse>

    @GET("hs/bss/v1/brands")
    suspend fun getBrands(
    ): Response<BrandsResponse>

    @GET("hs/bss/v1/brands/{brand_id}")
    suspend fun getBrandInfo(
        //@Header("brand_id") brandIdHeader: String,
        @Path(value = "brand_id") brandId: String
    ): Response<BrandInfoResponse>
}