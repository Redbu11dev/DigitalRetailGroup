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

    @GET("accounts")
    suspend fun getProfileInfo(
        @Header("user_id") userId: String
    ): Response<AccountsGetResponseDto>

    @POST("devices")
    suspend fun saveDeviceInfo(
        @Body devicesDto: DevicesDto
    ): Response<GenericResponseDto>

    @GET("balance")
    suspend fun getBalance(
        @Header("user_id") userId: String
    ): Response<BalanceResponseDto>

    @GET("code")
    suspend fun getCode(
        @Header("user_id") userId: String
    ): Response<CodeResponseDto>

    @GET("rules/increase")
    suspend fun getSavePointsRules(
        @Header("user_id") userId: String
    ): Response<RulesResponseDto>

    @GET("rules/decrease")
    suspend fun getSpendPointsRules(
        @Header("user_id") userId: String
    ): Response<RulesResponseDto>

    @GET("rules/promotion")
    suspend fun getPromotionRules(
        @Header("user_id") userId: String
    ): Response<RulesResponseDto>

    @GET("brands")
    suspend fun getBrands(
        @Header("user_id") userId: String
    ): Response<BrandsResponseDto>

    @GET("brands/{brand_id}")
    suspend fun getBrandInfo(
        //@Header("brand_id") brandIdHeader: String,
        @Header("user_id") userId: String,
        @Path(value = "brand_id") brandId: String
    ): Response<BrandInfoResponseDto>

    @GET("stores")
    suspend fun getRegionStores(
        @Header("user_id") userId: String,
        @Header("brand_id") brandId: String,
        @Header("region_id") regionId: String
    ): Response<StoresResponseDto>

    @GET("stores/{store_id}")
    suspend fun getRegionStoreInfo(
        @Header("user_id") userId: String,
        @Path("store_id") storeId: String
    ): Response<StoreInfoResponseDto>

    @GET("notifications")
    suspend fun getNotifications(
        @Header("user_id") userId: String,
        @Header("page") page: Int
    ): Response<NotificationsResponseDto>

    @POST("notifications")
    suspend fun markNotificationRead(
        @Body notificationPostDto: NotificationPostDto
    ): Response<GenericResponseDto>

    @DELETE("notifications/{notification_id}")
    suspend fun deleteNotification(
        @Header("user_id") userId: String,
        @Path("notification_id") notification_id: String
    ): Response<GenericResponseDto>
}