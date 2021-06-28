package com.vashkpi.digitalretailgroup.data.models.domain

import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.network.UserInfoDto
import timber.log.Timber

fun UserInfo.asNetworkModel(): UserInfoDto {
    return UserInfoDto(
        name,
        surname,
        middle_name,
        date_of_birth.convertDateToNetworkFormat(),
        gender
    )
}

fun String.convertDateToNetworkFormat(): String {
    if (this.length != 10) {
        return ""
    }
    else {
        val original = this.toCharArray()
        val day = String(charArrayOf(original[0], original[1]))
        val month = String(charArrayOf(original[3], original[4]))
        val year = String(charArrayOf(original[6], original[7], original[8], original[9]))
        return "$year.$month.$day"
    }
}

data class UserInfo(
    val name: String,
    val surname: String,
    val middle_name: String,
    val date_of_birth: String, //formats: local - "dd.mm.yyyy" | server - yyyy.mm.dd
    val gender: String, //по-русски "Мужской"/"Женский"
)

//other
enum class GenderValues(val value: String) {
    MALE("Мужской"),
    FEMALE("Женский")
}

fun Int.convertGenderRadioGroupIdToString(): String {
    return when (this) {
        R.id.radio_male -> {
            GenderValues.MALE.value
        }
        R.id.radio_female -> {
            GenderValues.FEMALE.value
        }
        else -> {
            ""
        }
    }
}

fun String.convertGenderStringToRadioGroupId(): Int {
    return when (this) {
        GenderValues.FEMALE.value -> {
            R.id.radio_female
        }
        GenderValues.MALE.value -> {
            R.id.radio_male
        }
        else -> {
            -1
        }
    }
}
