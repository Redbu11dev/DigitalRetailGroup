package com.vashkpi.digitalretailgroup.data.models.datastore

data class UserInfo(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //"11.11.1111"
    val gender: String, //по-русски "мужской"/"женский"
)
