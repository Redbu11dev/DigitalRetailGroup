package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.StoreEntity
import com.vashkpi.digitalretailgroup.data.models.domain.UserInfo

fun UserInfoDto.asDomainModel(): UserInfo {
    return UserInfo(
        name,
        surname,
        middle_name,
        date_of_birth,
        gender
    )
}

data class UserInfoDto(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //"1111.11.30"
    val gender: String, //по-русски "мужской"/"женский"
)
