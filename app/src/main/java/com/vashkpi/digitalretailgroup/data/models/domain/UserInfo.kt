package com.vashkpi.digitalretailgroup.data.models.domain

import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.network.UserInfoDto

fun UserInfo.asNetworkModel(): UserInfoDto {
    return UserInfoDto(
        name,
        surname,
        middle_name,
        date_of_birth,
        gender
    )
}

data class UserInfo(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //"30.11.1111" "1111.11.30"
    val gender: String, //по-русски "мужской"/"женский"
)

fun Int.convertGenderRadioGroupIdToString(): String {
    return when (this) {
        R.id.radio_male -> {
            AppConstants.GenderValues.MALE.value
        }
        R.id.radio_female -> {
            AppConstants.GenderValues.FEMALE.value
        }
        else -> {
            ""
        }
    }
}

fun String.convertGenderStringToRadioGroupId(): Int {
    return when (this) {
        AppConstants.GenderValues.FEMALE.value -> {
            R.id.radio_female
        }
        AppConstants.GenderValues.MALE.value -> {
            R.id.radio_male
        }
        else -> {
            -1
        }
    }
}
