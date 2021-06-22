package com.vashkpi.digitalretailgroup.data.models.network

import android.os.Parcelable
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import kotlinx.parcelize.Parcelize

fun BrandDto.asDatabaseModel(): BrandEntity {
    return BrandEntity(
        name,
        brand_id,
        image_parth,
        order
    )
}

@Parcelize
data class BrandDto (
    val name: String,
    val brand_id: String,
    val image_parth: String?, //typo on the server side
    val order: Int,
) : Parcelable