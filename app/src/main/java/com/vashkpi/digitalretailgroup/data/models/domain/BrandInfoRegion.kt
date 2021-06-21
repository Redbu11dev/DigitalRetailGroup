package com.vashkpi.digitalretailgroup.data.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrandInfoRegion(
    val name: String,
    val region_id: String,
    val order: Int
): Parcelable
