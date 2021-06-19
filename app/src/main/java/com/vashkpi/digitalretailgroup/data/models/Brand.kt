package com.vashkpi.digitalretailgroup.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand (
    val name: String,
    val brand_id: String,
    val image_parth: String, //typo on the server side
    val order: Int,
) : Parcelable