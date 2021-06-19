package com.vashkpi.digitalretailgroup.data.models.database

import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion

fun BrandInfoRegionEntity.asDomainModel(): BrandInfoRegion {
    return BrandInfoRegion(
        name,
        region_id,
        order
    )
}

data class BrandInfoRegionEntity(
    val name: String,
    val region_id: String,
    val order: Int
)
