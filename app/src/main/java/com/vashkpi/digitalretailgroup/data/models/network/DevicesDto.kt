package com.vashkpi.digitalretailgroup.data.models.network

data class DevicesDto (
    val user_id: String,
    val token: String, //FCM token
    val device_id: String,
    val os: String
)