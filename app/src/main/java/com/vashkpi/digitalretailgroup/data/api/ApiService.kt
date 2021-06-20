package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.network.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
//    @POST("hs/bss/v1/register")
//    suspend fun registerPhone(
//        @Body registerPhone: RegisterPhone
//    ): Response<JsonElement>

    @POST("register")
    suspend fun registerPhone(
        @Body registerPhoneDto: RegisterPhoneDto
    ): Response<GenericResponseDto>

    @POST("confirm")
    suspend fun confirmCode(
        @Body confirmCodeDto: ConfirmCodeDto
    ): Response<ConfirmCodeResponseDto>

    @POST("accounts")
    suspend fun saveProfileInfo(
        @Body accountsDto: AccountsDto
    ): Response<GenericResponseDto>

    @GET("brands")
    suspend fun getBrands(
    ): Response<BrandsResponseDto>

    @GET("brands/{brand_id}")
    suspend fun getBrandInfo(
        //@Header("brand_id") brandIdHeader: String,
        @Path(value = "brand_id") brandId: String
    ): Response<BrandInfoResponseDto>

    @GET("stores")
    suspend fun getRegionStores(
        @Header("brand_id") brandId: String,
        @Header("region_id") regionId: String
    ): Response<StoresResponseDto>

    @GET("stores/{store_id}")
    suspend fun getRegionStoreInfo(
        @Path("store_id") storeId: String
    ): Response<StoreInfoResponseDto>

    @GET("notifications")
    suspend fun getNotifications(
        @Header("user_id") userId: String,
        @Header("page") page: Int
    ): Response<NotificationsResponseDto>
}