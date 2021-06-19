package com.vashkpi.digitalretailgroup.data.models.database

import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.network.BrandsResponse

fun BrandEntity.asDomainModel(): Brand {
    return Brand(
        name,
        brand_id,
        image_parth,
        order
    )
}

data class BrandEntity (
    val name: String,
    val brand_id: String,
    val image_parth: String, //typo on the server side
    val order: Int
)