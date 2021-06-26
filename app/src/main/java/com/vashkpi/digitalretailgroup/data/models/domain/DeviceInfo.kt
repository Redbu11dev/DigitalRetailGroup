package com.vashkpi.digitalretailgroup.data.models.domain


import com.vashkpi.digitalretailgroup.data.models.network.DevicesDto

fun DeviceInfo.asNetworkModel(): DevicesDto {
    return DevicesDto(
        user_id,
        token,
        device_id,
        os
    )
}

data class DeviceInfo (
    val user_id: String,
    val token: String, //FCM token
    val device_id: String,
    val os: String
)