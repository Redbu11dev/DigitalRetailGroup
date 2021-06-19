package com.vashkpi.digitalretailgroup.data.models.domain

data class ConfirmCode(
    val phone: String,
    val code: String,
    val device_id: String,
    val os: String
)
