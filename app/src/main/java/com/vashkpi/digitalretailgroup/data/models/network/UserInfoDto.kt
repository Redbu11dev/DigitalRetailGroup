package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.domain.UserInfo
import timber.log.Timber

fun UserInfoDto.asDomainModel(): UserInfo {
    return UserInfo(
        name,
        surname,
        middle_name,
        date_of_birth.convertDateToDomainFormat(),
        gender
    )
}

fun String.convertDateToDomainFormat(): String {
    val original = this.toCharArray()
    val year = String(charArrayOf(original[0], original[1], original[2], original[3]))
    val month = String(charArrayOf(original[5], original[6]))
    val day = String(charArrayOf(original[8], original[9]))
    return "$day.$month.$year"
}

data class UserInfoDto(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //formats: local - "30.11.1111" | server - bullshit
    val gender: String, //по-русски "Мужской"/"Женский"
)
