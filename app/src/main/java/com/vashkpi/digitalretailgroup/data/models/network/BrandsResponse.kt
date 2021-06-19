package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.network.dto.BrandDto

fun BrandsResponse.asDatabaseModel(): List<BrandEntity> {
    return elements.map {
        BrandEntity(
            it.name,
            it.brand_id,
            it.image_parth,
            it.order
        )
    }
}

data class BrandsResponse (
    val elements: List<BrandDto>
)