package com.vashkpi.digitalretailgroup.data.models.network

data class ConfirmCodeDto(
    val phone: String,
    val code: String,
    val device_id: String,
    val os: String
)
