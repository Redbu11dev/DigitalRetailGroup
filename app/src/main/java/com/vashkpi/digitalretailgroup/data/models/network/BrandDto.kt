package com.vashkpi.digitalretailgroup.data.models.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrandDto (
    val name: String,
    val brand_id: String,
    val image_parth: String, //typo on the server side
    val order: Int,
) : Parcelable