package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.StoreEntity
import com.vashkpi.digitalretailgroup.data.models.domain.UserInfo

fun UserInfoDto.asDomainModel(): UserInfo {
    return UserInfo(
        name,
        surname,
        middle_name,
        date_of_birth,
        gender.convertGenderToDomainFormat()
    )
}

fun String.convertGenderToDomainFormat(): String {
    val original = this
    val year = original.subSequence(0,3)
    val month = original.subSequence(5,6)
    val day = original.subSequence(8,9)


    return "$day.$month.$year"
}

data class UserInfoDto(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //formats: local - "30.11.1111" | server - "1111-11-30"
    val gender: String, //по-русски "Мужской"/"Женский"
)
