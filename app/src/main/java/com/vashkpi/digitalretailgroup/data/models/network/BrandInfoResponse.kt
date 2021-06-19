package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfo
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion
import com.vashkpi.digitalretailgroup.data.models.network.dto.BrandInfoRegionDto

fun BrandInfoResponse.asDatabaseModel(): BrandInfo {
    return BrandInfo(
        name,
        website,
        telephone,
        time_of_work,
        regions.map {
            BrandInfoRegion(
                it.name,
                it.region_id,
                it.order,
            )
        }
    )
}

data class BrandInfoResponse(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegionDto>
)
