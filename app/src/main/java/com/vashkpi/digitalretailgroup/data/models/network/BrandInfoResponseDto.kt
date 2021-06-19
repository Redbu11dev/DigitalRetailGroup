package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoRegionEntity

fun BrandInfoResponseDto.asDatabaseModel(): BrandInfoEntity {
    return BrandInfoEntity(
        name,
        website,
        telephone,
        time_of_work,
        regions.map {
            BrandInfoRegionEntity(
                it.name,
                it.region_id,
                it.order,
            )
        }
    )
}

data class BrandInfoResponseDto(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegionDto>
)
