package com.vashkpi.digitalretailgroup.data.models.network.dto

import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R

data class UserInfoDto(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //"30.11.1111" "1111.11.30"
    val gender: String, //по-русски "мужской"/"женский"
)
