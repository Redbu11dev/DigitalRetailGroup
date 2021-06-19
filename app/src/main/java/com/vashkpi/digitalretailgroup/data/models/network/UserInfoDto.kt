package com.vashkpi.digitalretailgroup.data.models.network


data class UserInfoDto(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //"1111.11.30"
    val gender: String, //по-русски "мужской"/"женский"
)
