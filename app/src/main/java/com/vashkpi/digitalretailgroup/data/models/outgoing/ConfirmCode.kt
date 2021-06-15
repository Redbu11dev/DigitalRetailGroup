package com.vashkpi.digitalretailgroup.data.models.outgoing

data class ConfirmCode(
    val phone: String,
    val code: String,
    val device_id: String,
    val os: String
)
